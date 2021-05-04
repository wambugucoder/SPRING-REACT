package com.server.pollingapp.service;

import com.server.pollingapp.models.ChoiceModel;
import com.server.pollingapp.models.PollModel;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TwitterService {

    final Twitter twitter;

    public TwitterService(Twitter twitter) {
        this.twitter = twitter;
    }

    public void SendResults(PollModel pollModel){
        String question= pollModel.getQuestion();
        String author=pollModel.getCreatedBy().getUsername();
        String createdAt=pollModel.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME).toString();
        String text="A poll with id"+""+pollModel.getId()+":"+question+","+"created by"+""+author+""+"at"+""+
                createdAt+""+"just closed.To view results click the following link";

        twitter.timelineOperations().updateStatus(text);
    }
}
