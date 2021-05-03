package com.server.pollingapp.schedules;

import com.server.pollingapp.models.PollModel;
import com.server.pollingapp.models.PollStatus;
import com.server.pollingapp.models.PollsCategory;
import com.server.pollingapp.repository.PollRepository;
import com.server.pollingapp.service.PollStream;
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

    public PollSchedule(PollRepository pollRepository, PollStream pollStream) {
        this.pollRepository = pollRepository;
        this.pollStream = pollStream;
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
    }



