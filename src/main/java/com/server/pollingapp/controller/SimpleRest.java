package com.server.pollingapp.controller;


import com.server.pollingapp.models.PollModel;
import com.server.pollingapp.models.PollStatus;
import com.server.pollingapp.repository.PollRepository;
import com.server.pollingapp.security.SecuredController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SimpleRest implements SecuredController{

    final PollRepository pollRepository;

    public SimpleRest(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @GetMapping(value = "/api/v1/hello")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok().body("it works");
    }

    @GetMapping(value = "/api/v1/opened-polls",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PollModel> getOpenPolls(){
        return pollRepository.findAllByPollStatusEquals(PollStatus.POLL_OPENED);
    }
}
