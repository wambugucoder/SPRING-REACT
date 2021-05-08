package com.server.pollingapp.request;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class NonScheduledPollRequest implements Serializable {
    @NotNull(message = "Question cannot be null")
    private String question;

    @Size(min = 2,max=6,message = "A min of 2 and a max of 6 Options are Required")
    private List<ChoiceRequest> options;

    @Future(message = "Closing Time has to in the future ")
    @NotNull(message = "Closing Time cannot be empty")
    private LocalDateTime closingTime;


    public NonScheduledPollRequest(String question, List<ChoiceRequest> options, LocalDateTime closingTime) {
        this.question = question;
        this.options = options;
        this.closingTime = closingTime;
    }

    public String getQuestion() {
        return question;
    }

    public List<ChoiceRequest> getOptions() {
        return options;
    }

    public LocalDateTime getClosingTime() {
        return closingTime;
    }


}
