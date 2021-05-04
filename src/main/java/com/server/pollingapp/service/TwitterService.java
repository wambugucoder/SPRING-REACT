package com.server.pollingapp.service;

import com.server.pollingapp.models.PollModel;
import com.server.pollingapp.request.RealTimeLogRequest;
import org.springframework.social.twitter.api.InvalidMessageRecipientException;
import org.springframework.social.twitter.api.MessageTooLongException;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TwitterService {

    final Twitter twitter;

    final PollStream pollStream;

    public TwitterService(Twitter twitter, PollStream pollStream) {
        this.twitter = twitter;
        this.pollStream = pollStream;
    }

    public void SendNotification(PollModel pollModel){
        try {
            twitter.directMessageOperations().sendDirectMessage("JosWambugu","Poll with id:"+""+pollModel.getId() +" "+"Has just been Closed at"+" "+LocalDateTime.now().toString());
        }
        catch (Exception e){
            pollStream.sendToMessageBroker(new RealTimeLogRequest("ERROR",e.getMessage(),"TwitterService"));
        }
        }

}
