package com.server.pollingapp.controller;


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

/**
 * @author Jos Wambugu
 * @since 30-04-2021
 * All poll endpoints should be placed here
 */
@RestController
public class PollController implements SecuredController {

    @Autowired
    PollService pollService;

    @PostMapping(value = "/api/v1/polls/{userId}/non-scheduled-poll",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UniversalResponse> NonScheduledPoll(@RequestBody @Valid NonScheduledPollRequest nonScheduledPollRequest, @PathVariable String userId, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            UniversalResponse universalResponse=new UniversalResponse();
            universalResponse.setMessage("Please check the details you provided");
            universalResponse.setError(true);
            return ResponseEntity.badRequest().body(universalResponse);
        }
        return  pollService.CreateNonScheduledPoll(nonScheduledPollRequest,userId);

    }
    @PostMapping(value = "/api/v1/polls/{userId}/scheduled-poll",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UniversalResponse>ScheduledPoll(@RequestBody @Valid ScheduledPollRequest scheduledPollRequest, @PathVariable String userId, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            UniversalResponse universalResponse=new UniversalResponse();
            universalResponse.setMessage("Please check the details you provided");
            universalResponse.setError(true);
            return ResponseEntity.badRequest().body(universalResponse);
        }
        return  pollService.CreateScheduledPoll(scheduledPollRequest, userId);

    }
}
