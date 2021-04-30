package com.server.pollingapp.service;

import com.server.pollingapp.models.*;
import com.server.pollingapp.repository.PollRepository;
import com.server.pollingapp.repository.UserRepository;
import com.server.pollingapp.request.NonScheduledPollRequest;
import com.server.pollingapp.request.RealTimeLogRequest;
import com.server.pollingapp.request.ScheduledPollRequest;
import com.server.pollingapp.response.UniversalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PollService {
    final PollRepository pollRepository;

    final UserRepository userRepository;

    final UserRepositoryImpl userRepositoryImp;

    final PollStream pollStream;

    final SentimentAnalysisService sentimentAnalysisService;

    public PollService(PollRepository pollRepository, UserRepository userRepository, UserRepositoryImpl userRepositoryImp, PollStream pollStream, SentimentAnalysisService sentimentAnalysisService) {
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
        this.userRepositoryImp = userRepositoryImp;
        this.pollStream = pollStream;
        this.sentimentAnalysisService = sentimentAnalysisService;
    }

    private UserModel fetchUserId(String userId) {
        return userRepositoryImp.findUserById(userId);
    }

    private Boolean AnalyzePollContents(String text) {
        return sentimentAnalysisService.GetSentimentScoreOfPoll(text) >= 2;
    }

    public ResponseEntity<UniversalResponse> CreateNonScheduledPoll(NonScheduledPollRequest nonScheduledPollRequest, String userId) {
        //ANALYZE POLL CONTENT
        if (AnalyzePollContents(nonScheduledPollRequest.getQuestion())) {

            // FIND AUTHOR DETAILS
            UserModel author = fetchUserId(userId);

            //SET UP CHOICES
            List<ChoiceModel> list = new ArrayList<ChoiceModel>();

            //ADD CHOICES TO LIST<CHOICE MODEL>
            nonScheduledPollRequest.getOptions().stream().map(choiceRequest -> new ChoiceModel(choiceRequest.getOption())).forEachOrdered(list::add);


            //CREATE POLL INSTANCE
            PollModel pollModel = new PollModel();
            pollModel.setCategory(PollsCategory.NON_SCHEDULED_POLL);
            pollModel.setPollStatus(PollStatus.POLL_OPENED);
            pollModel.setClosingTime(nonScheduledPollRequest.getClosingTime());
            pollModel.setCreatedBy(author);
            pollModel.setOptions(list);
            pollModel.setQuestion(nonScheduledPollRequest.getQuestion());

            //SAVE POLL
            try {
                pollRepository.save(pollModel);
            } catch (IllegalArgumentException e) {
                //GENERATE LOGS
                pollStream.sendToMessageBroker(new RealTimeLogRequest("WARN", e.getMessage(), "PollService"));
                UniversalResponse error = new UniversalResponse();
                error.setError(true);
                error.setMessage(e.getMessage());
                return ResponseEntity.badRequest().body(error);
            }
            //ON SUCCESS->SEND 200
            //GENERATE LOGS
            pollStream.sendToMessageBroker(new RealTimeLogRequest("INFO", "Poll Created Successfully", "PollService"));
            UniversalResponse success = new UniversalResponse();
            success.setError(false);
            success.setMessage("Poll Created Successfully");
            return ResponseEntity.ok().body(success);
        }
        pollStream.sendToMessageBroker(new RealTimeLogRequest("WARN", "Some Poll content was rejected", "PollService"));
        UniversalResponse error = new UniversalResponse();
        error.setError(true);
        error.setMessage("This Poll violates our guidelines.Please change the question");
        return ResponseEntity.badRequest().body(error);

    }

    public ResponseEntity<UniversalResponse> CreateScheduledPoll(ScheduledPollRequest scheduledPollRequest, String userId) {
        //ANALYZE POLL CONTENT
        if (AnalyzePollContents(scheduledPollRequest.getQuestion())) {

            // FIND AUTHOR DETAILS
            UserModel author = fetchUserId(userId);

            //SET UP CHOICES
            List<ChoiceModel> list = new ArrayList<ChoiceModel>();

            //ADD CHOICES TO LIST<CHOICE MODEL>
            scheduledPollRequest.getOptions().stream().map(choiceRequest -> new ChoiceModel(choiceRequest.getOption())).forEachOrdered(list::add);


            //CREATE POLL INSTANCE
            PollModel pollModel = new PollModel();
            pollModel.setCategory(PollsCategory.SCHEDULED_POLL);
            pollModel.setPollStatus(PollStatus.POLL_PENDING);
            pollModel.setScheduledTime(scheduledPollRequest.getScheduledTime());
            pollModel.setClosingTime(scheduledPollRequest.getClosingTime());
            pollModel.setCreatedBy(author);
            pollModel.setOptions(list);
            pollModel.setQuestion(scheduledPollRequest.getQuestion());

            //SAVE POLL
            try {
                pollRepository.save(pollModel);
            } catch (IllegalArgumentException e) {
                //GENERATE LOGS
                pollStream.sendToMessageBroker(new RealTimeLogRequest("WARN", e.getMessage(), "PollService"));
                UniversalResponse error = new UniversalResponse();
                error.setError(true);
                error.setMessage(e.getMessage());
                return ResponseEntity.badRequest().body(error);
            }
            //ON SUCCESS->SEND 200
            //GENERATE LOGS
            pollStream.sendToMessageBroker(new RealTimeLogRequest("INFO", "Poll Has Been Scheduled Successfully", "PollService"));
            UniversalResponse success = new UniversalResponse();
            success.setError(false);
            success.setMessage("Poll Has been Scheduled For," + pollModel.getScheduledTime());
            return ResponseEntity.ok().body(success);


        }
        pollStream.sendToMessageBroker(new RealTimeLogRequest("WARN", "Some Poll content was rejected", "PollService"));
        UniversalResponse error = new UniversalResponse();
        error.setError(true);
        error.setMessage("This Poll violates our guidelines.Please change the question");
        return ResponseEntity.badRequest().body(error);

    }
}