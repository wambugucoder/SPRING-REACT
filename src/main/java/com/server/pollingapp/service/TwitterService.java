package com.server.pollingapp.service;

import com.server.pollingapp.models.PollModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TwitterService {

    final Twitter twitter;

    @Autowired
    public TwitterService(@Lazy Twitter twitter) {
        this.twitter = twitter;
    }

    public void SendNotification(PollModel pollModel){
        try {
            twitter.directMessageOperations().sendDirectMessage("JosWambugu","Poll with id:"+""+pollModel.getId() +" "+"Has just been Closed at"+" "+LocalDateTime.now().toString());
        }
        catch (Exception e){

        }
        }

}
