package com.server.pollingapp.request;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ScheduledPollRequest implements Serializable {
  @NotNull(message = "Question cannot be null") private String question;

  @Size(min = 2, max = 6,
        message = "A min of 2 and a max of 6 Options are Required")
  private List<ChoiceRequest> options;

  @Future(message = "Scheduled Time has to in the future ")
  @NotNull(message = "ScheduledTime cannot be empty")
  private LocalDateTime scheduledTime;

  @Future(message = "Closing Time has to in the future ")
  @NotNull(message = "Closing Time cannot be empty")
  private LocalDateTime closingTime;

  public ScheduledPollRequest(String question, List<ChoiceRequest> options,
                              LocalDateTime scheduledTime,
                              LocalDateTime closingTime) {
    this.question = question;
    this.options = options;
    this.scheduledTime = scheduledTime;
    this.closingTime = closingTime;
  }

  public String getQuestion() { return question; }

  public void setQuestion(String question) { this.question = question; }

  public List<ChoiceRequest> getOptions() { return options; }

  public LocalDateTime getScheduledTime() { return scheduledTime; }

  public LocalDateTime getClosingTime() { return closingTime; }
}
