package com.server.pollingapp.service;

import com.server.pollingapp.models.PollModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TwitterService {

    final Twitter twitter;
    Logger log= LoggerFactory.getLogger(TwitterService.class);

    @Autowired
    public TwitterService(@Lazy Twitter twitter) {
        this.twitter = twitter;
    }

    public void SendNotification(PollModel pollModel){
        try {
            twitter.directMessageOperations().sendDirectMessage("JosWambugu","Poll with id:"+""+pollModel.getId() +" "+"Has just been Closed at"+" "+LocalDateTime.now().toString());
        }
        catch (Exception e){
         log.error("Couldn't Sent Message");
        }
        }

}
