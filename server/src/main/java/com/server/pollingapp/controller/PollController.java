package com.server.pollingapp.controller;


import com.server.pollingapp.models.PollModel;
import com.server.pollingapp.models.PollStatus;
import com.server.pollingapp.request.NonScheduledPollRequest;
import com.server.pollingapp.request.ScheduledPollRequest;
import com.server.pollingapp.response.UniversalResponse;
import com.server.pollingapp.security.SecuredController;
import com.server.pollingapp.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Jos Wambugu
 * @since 30-04-2021
 * All poll endpoints should be placed here
 */
@RestController
public class PollController implements SecuredController {


    @Autowired PollService pollService;



    @PostMapping(value = "/api/v1/polls/{userId}/non_scheduled_poll",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UniversalResponse> NonScheduledPoll(@RequestBody @Valid NonScheduledPollRequest nonScheduledPollRequest, @PathVariable String userId, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            UniversalResponse universalResponse=new UniversalResponse();
            universalResponse.setMessage("Please check the details you provided");
            universalResponse.setError(true);
            return ResponseEntity.badRequest().body(universalResponse);
        }
        return  pollService.CreateNonScheduledPoll(nonScheduledPollRequest,userId);

    }
    @PostMapping(value = "/api/v1/polls/{userId}/scheduled_poll",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UniversalResponse>ScheduledPoll(@RequestBody @Valid ScheduledPollRequest scheduledPollRequest, @PathVariable String userId, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            UniversalResponse universalResponse=new UniversalResponse();
            universalResponse.setMessage("Please check the details you provided");
            universalResponse.setError(true);
            return ResponseEntity.badRequest().body(universalResponse);
        }
        return  pollService.CreateScheduledPoll(scheduledPollRequest, userId);

    }
    @GetMapping(value = "/api/v1/polls/opened_polls",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PollModel> GetAllOpenPolls(){
        return pollService.GetAllOpenPolls(PollStatus.POLL_OPENED);
    }

    @GetMapping(value = "/api/v1/polls/closed_polls",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PollModel> GetAllClosedPolls(){
        return pollService.GetClosedPolls(PollStatus.POLL_CLOSED);
    }

    @GetMapping(value = "/api/v1/polls/scheduled_polls",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PollModel> GetAllScheduledPolls(){
        return pollService.GetScheduledPolls(PollStatus.POLL_PENDING);
    }

    @GetMapping(value = "/api/v1/polls/specific_poll/{pollId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Object GetSpecificPoll(@PathVariable String pollId){
        return pollService.GetPollById(pollId);
    }

    @PostMapping(value = "/api/v1/polls/cast_vote/{userId}/{pollId}/{choiceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UniversalResponse> CastVote(@PathVariable String choiceId, @PathVariable String pollId, @PathVariable String userId){
        return pollService.CastVote(pollId,choiceId,userId);
    }
}
