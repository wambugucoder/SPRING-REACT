package com.server.pollingapp.service;

import com.server.pollingapp.models.*;
import com.server.pollingapp.repository.ChoiceRepository;
import com.server.pollingapp.repository.PollRepository;
import com.server.pollingapp.repository.UserRepository;
import com.server.pollingapp.repository.VotesRepository;
import com.server.pollingapp.request.NonScheduledPollRequest;
import com.server.pollingapp.request.ScheduledPollRequest;
import com.server.pollingapp.response.UniversalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PollService {
    @Autowired PollRepository pollRepository;

    @Autowired UserRepository userRepository;

    @Autowired UserRepositoryImpl userRepositoryImp;

    @Autowired SentimentAnalysisService sentimentAnalysisService;

    @Autowired ChoiceRepository choiceRepository;

    @Autowired VotesRepository votesRepository;

    Logger log=  LoggerFactory.getLogger(PollService.class);


    private UserModel fetchUserId(String userId) {
        return userRepositoryImp.findUserById(userId);
    }

    private Boolean AnalyzePollContents(String text) {
        return sentimentAnalysisService.GetSentimentScoreOfPoll(text) >= 2;
    }

    public ResponseEntity<UniversalResponse> CreateNonScheduledPoll(NonScheduledPollRequest nonScheduledPollRequest, String userId) {
        //ANALYZE POLL CONTENT
        Boolean goodContent=AnalyzePollContents(nonScheduledPollRequest.getQuestion());
        if (Boolean.TRUE.equals(goodContent)) {

            // FIND AUTHOR DETAILS
            UserModel author = fetchUserId(userId);

            //SET UP CHOICES
            List<ChoiceModel> list = new ArrayList<ChoiceModel>();

            //CREATE POLL INSTANCE
            PollModel pollModel = new PollModel();
            //ADD CHOICES TO LIST<CHOICE MODEL>
            nonScheduledPollRequest.getOptions().stream().map(choiceRequest -> new ChoiceModel(choiceRequest.getOption(), pollModel)).forEachOrdered(list::add);

            pollModel.setCategory(PollsCategory.NON_SCHEDULED_POLL);
            pollModel.setPollStatus(PollStatus.POLL_OPENED);
            pollModel.setClosingTime(nonScheduledPollRequest.getClosingTime());
            pollModel.setCreatedBy(author);
            pollModel.setOptions(list);
            pollModel.setQuestion(nonScheduledPollRequest.getQuestion());


            //SAVE POLL
         
           pollRepository.save(pollModel);

           
            //ON SUCCESS->SEND 200
            //GENERATE LOGS
            log.info("Non-Scheduled Poll Created Successfully");
            UniversalResponse success = new UniversalResponse();
            success.setError(false);
            success.setMessage("Poll Created Successfully");
            return ResponseEntity.ok().body(success);
        }

        UniversalResponse error = new UniversalResponse();
        error.setError(true);
        error.setMessage("This Poll violates our guidelines.Please change the question");
        return ResponseEntity.badRequest().body(error);

    }

    public ResponseEntity<UniversalResponse> CreateScheduledPoll(ScheduledPollRequest scheduledPollRequest, String userId) {
        //ANALYZE POLL CONTENT
        Boolean goodContent=AnalyzePollContents(scheduledPollRequest.getQuestion());
        if (Boolean.TRUE.equals(goodContent)) {

            // FIND AUTHOR DETAILS
            UserModel author = fetchUserId(userId);

            //SET UP CHOICES
            List<ChoiceModel> list = new ArrayList<>();


            //CREATE POLL INSTANCE
            PollModel pollModel = new PollModel();

            //ADD CHOICES TO LIST<CHOICE MODEL>
            scheduledPollRequest.getOptions().stream().map(choiceRequest -> new ChoiceModel(choiceRequest.getOption(), pollModel)).forEachOrdered(list::add);


            pollModel.setCategory(PollsCategory.SCHEDULED_POLL);
            pollModel.setPollStatus(PollStatus.POLL_PENDING);
            pollModel.setScheduledTime(scheduledPollRequest.getScheduledTime());
            pollModel.setClosingTime(scheduledPollRequest.getClosingTime());
            pollModel.setCreatedBy(author);
            pollModel.setOptions(list);
            pollModel.setQuestion(scheduledPollRequest.getQuestion());


            //SAVE POLL AND CHOICES
            
             pollRepository.save(pollModel);
           
            //ON SUCCESS->SEND 200
            //GENERATE LOGS
            log.info("Created a Scheduled Poll");
            UniversalResponse success = new UniversalResponse();
            success.setError(false);
            success.setMessage("Poll Has been Scheduled For," + pollModel.getScheduledTime());
            return ResponseEntity.ok().body(success);


        }

        UniversalResponse error = new UniversalResponse();
        error.setError(true);
        error.setMessage("This Poll violates our guidelines.Please change the question");
        return ResponseEntity.badRequest().body(error);

    }

    public List<PollModel> GetAllOpenPolls(PollStatus pollStatus) {
        return pollRepository.findAllByPollStatusEqualsOrderByCreatedAtDesc(pollStatus);
    }

    public ResponseEntity<UniversalResponse> CastVote(String pollId, String choiceId, String userId) {
        PollModel poll = pollRepository.getOne(pollId);
        UserModel user = fetchUserId(userId);
        ChoiceModel choice = choiceRepository.getOne(choiceId);

        Optional<VotesModel> findIfUserVoted = poll.getVotes().stream().filter(votes -> (votes.getUser().getId().equalsIgnoreCase(userId)))
                .findFirst();
        //IF USER DOESNT EXIST IN VOTER REGISTRY SAVE THEM
        if (!findIfUserVoted.isPresent()) {
            //CREATE VOTE INSTANCE
            VotesModel votesModel = new VotesModel();
            votesModel.setChoice(choice);
            votesModel.setPoll(poll);
            votesModel.setUser(user);


            
                poll.addVotes(votesModel);
                //CHOICES
                choice.addVotes(votesModel);
                choice.setPolls(poll);

                //SAVE TO DB
                choiceRepository.save(choice);


            //IF USER DIDN'T VOTE
            //CAST VOTE AND RETURN A 200
            UniversalResponse success = new UniversalResponse();
            success.setError(false);
            success.setMessage("Your Vote has Been Placed");
            return ResponseEntity.ok().body(success);

        }
        UniversalResponse error = new UniversalResponse();
        error.setMessage("You Seem to have already voted");
        error.setError(true);
        return ResponseEntity.badRequest().body(error);
    }

    public Optional<PollModel> GetPollById(String id){
        return pollRepository.findById(id);

    }
    public List<PollModel> GetClosedPolls(PollStatus pollStatus){
        return pollRepository.findAllByPollStatusEqualsOrderByCreatedAtDesc(pollStatus);
    }

    public List<PollModel> GetScheduledPolls(PollStatus pollStatus){
        return pollRepository.findAllByPollStatusEqualsOrderByCreatedAtDesc(pollStatus);
    }

}