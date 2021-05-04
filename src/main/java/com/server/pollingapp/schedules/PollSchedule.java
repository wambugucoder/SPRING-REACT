package com.server.pollingapp.schedules;

import com.server.pollingapp.models.PollModel;
import com.server.pollingapp.models.PollStatus;
import com.server.pollingapp.models.PollsCategory;
import com.server.pollingapp.repository.PollRepository;
import com.server.pollingapp.service.PollStream;
import com.server.pollingapp.service.TwitterService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Jos Wambugu
 * @since 01/05/2021
 */
@Component
public class PollSchedule {

    final PollStream pollStream;
    final PollRepository pollRepository;
    final TwitterService twitterService;
    public PollSchedule(PollRepository pollRepository, PollStream pollStream, TwitterService twitterService) {
        this.pollRepository = pollRepository;
        this.pollStream = pollStream;
        this.twitterService = twitterService;
    }



    /**
     * Once a poll has reached its closing Time,This scheduled service that runs every second
     * should be able to track it and set status as closed.
     *
     */
    @Scheduled(fixedDelay = 1000)
    private void ClosePollsWhenClosingTimeArrives(){
        List<PollModel> polls=pollRepository.findAllByPollStatusEquals(PollStatus.POLL_OPENED);
        if (!polls.isEmpty()){
            polls.stream().filter(poll->(LocalDateTime.now().isAfter(poll.getClosingTime())||
                    poll.getClosingTime().isEqual(LocalDateTime.now())))
                    .peek(poll -> poll.setPollStatus(PollStatus.POLL_CLOSED))
                    .forEachOrdered(pollRepository::save);


        }

    }

    /**
     * Scheduled Polls Contain a Scheduled Open Time.
     * Track all POLL_SCHEDULED and makes sure the scheduled poll is opened at the correct time
     */
    @Scheduled(fixedDelay = 1000)
    private void OpenScheduledPolls(){
     List<PollModel> scheduledpolls=pollRepository.findAllByCategoryEquals(PollsCategory.SCHEDULED_POLL);
     if (!scheduledpolls.isEmpty()){
         scheduledpolls.stream().filter(eachpoll->(LocalDateTime.now().isAfter(eachpoll.getScheduledTime())||
                 eachpoll.getScheduledTime().isEqual(LocalDateTime.now())
         ))
                 .peek(eachpoll-> eachpoll.setPollStatus(PollStatus.POLL_OPENED))
                 .forEachOrdered(pollRepository::save);
     }
     }

    /**
     * Once A poll is Closed,Results should be posted to the "official" Twitter feed
     * If result has been posted,change poll status to POLL_CLOSED_AND_RESULTS SENT to
     * avoid resending.
     * Process runs after every 1 minute
     * Reason for sending results to twitter->To Notify Users and direct traffic towards the app.
     */
    private void SendResultsToTwitterFeed(){
        List<PollModel> closedPolls=pollRepository.findAllByPollStatusEquals(PollStatus.POLL_CLOSED);
        if (!closedPolls.isEmpty()){
            closedPolls.stream()
                    .peek(eachPoll->{
                        //SEND RESULTS TO TWITTER AND UPDATE STATUS
                        twitterService.SendNotification(eachPoll);
                        eachPoll.setPollStatus(PollStatus.POLL_CLOSED_AND_NOTIFICATION_SENT);
                    })
                    .forEachOrdered(pollRepository::save);
        }


    }

}


