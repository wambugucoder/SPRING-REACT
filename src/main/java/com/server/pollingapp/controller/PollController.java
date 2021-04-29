package com.server.pollingapp.controller;


import com.server.pollingapp.request.NonScheduledPollRequest;
import com.server.pollingapp.response.UniversalResponse;
import com.server.pollingapp.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PollController {

    @Autowired
    PollService pollService;

    @PostMapping("/api/v1/polls/{userId}/non-scheduled-poll")
    public ResponseEntity<UniversalResponse> NonScheduledPoll(@RequestBody @Valid NonScheduledPollRequest nonScheduledPollRequest, @PathVariable String userId, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            UniversalResponse universalResponse=new UniversalResponse();
            universalResponse.setMessage("Please check the details you provided");
            universalResponse.setError(true);
            return ResponseEntity.badRequest().body(universalResponse);
        }
        return  pollService.CreateNonScheduledPoll(nonScheduledPollRequest,userId);

    }
}
