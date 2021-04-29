package com.server.pollingapp.service;

import com.server.pollingapp.models.*;
import com.server.pollingapp.repository.PollRepository;
import com.server.pollingapp.repository.UserRepository;
import com.server.pollingapp.request.ChoiceRequest;
import com.server.pollingapp.request.NonScheduledPollRequest;
import com.server.pollingapp.response.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PollService {
    @Autowired
    PollRepository pollRepository;

    @Autowired
    UserRepository userRepository;


public ResponseEntity<UniversalResponse> CreateNonScheduledPoll(NonScheduledPollRequest nonScheduledPollRequest,String userId) {
    // FIND AUTHOR DETAILS
    UserModel author = userRepository.getOne(userId);

    //SET UP CHOICES
    List<ChoiceModel> choiceModel=new ArrayList<ChoiceModel>();

    //ADD CHOICES TO LIST<CHOICE MODEL>
   nonScheduledPollRequest.getOptions().stream().map(choiceRequest -> {
        ChoiceModel choice=new ChoiceModel();
        choice.setOption(choiceRequest.getOption());
      return choiceModel.add(choice) ;
    });


    //CREATE POLL INSTANCE
    PollModel pollModel = new PollModel();
    pollModel.setCategory(PollsCategory.NON_SCHEDULED_POLL);
    pollModel.setPollStatus(PollStatus.POLL_OPENED);
    pollModel.setClosingTime(nonScheduledPollRequest.getClosingTime());
    pollModel.setCreatedBy(author);
    pollModel.setOptions(choiceModel);
    pollModel.setQuestion(nonScheduledPollRequest.getQuestion());

    //SAVE POLL
    try {
        pollRepository.save(pollModel);
    }
    catch (IllegalArgumentException e){
       UniversalResponse error= new UniversalResponse();
       error.setError(true);
       error.setMessage(e.getMessage());
       return ResponseEntity.badRequest().body(error);
    }
    //ON SUCCESS
    UniversalResponse success= new UniversalResponse();
    success.setError(false);
    success.setMessage("Poll Created Successfully");
    return ResponseEntity.badRequest().body(success);




}
}
