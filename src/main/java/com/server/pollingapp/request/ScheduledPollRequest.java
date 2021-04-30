package com.server.pollingapp.request;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class ScheduledPollRequest {
    @NotNull(message = "Question cannot be null")
    private String question;

    @Size(min = 2,max=6,message = "A min of 2 and a max of 6 Options are Required")
    private List<ChoiceRequest> options;

    @Future(message = "Scheduled Time has to in the future ")
    @NotNull(message = "ScheduledTime cannot be empty")
    private LocalDateTime scheduledTime;

    @Future(message = "Closing Time has to in the future ")
    @NotNull(message = "Closing Time cannot be empty")
    private LocalDateTime closingTime;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<ChoiceRequest> getOptions() {
        return options;
    }

    public void setOptions(List<ChoiceRequest> options) {
        this.options = options;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public LocalDateTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalDateTime closingTime) {
        this.closingTime = closingTime;
    }
}
