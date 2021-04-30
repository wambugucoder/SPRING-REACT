package com.server.pollingapp.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "votes")
public class VotesModel implements Serializable {

    private static final long serialVersionUID = -7267712774522332916L;
    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id= UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id",referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PollModel poll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "choice_id",referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ChoiceModel choice;

    public VotesModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PollModel getPoll() {
        return poll;
    }

    public void setPoll(PollModel poll) {
        this.poll = poll;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public ChoiceModel getChoice() {
        return choice;
    }

    public void setChoice(ChoiceModel choice) {
        this.choice = choice;
    }
}
