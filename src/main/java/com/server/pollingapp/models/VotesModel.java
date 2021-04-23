package com.server.pollingapp.models;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "votes")
public class VotesModel {

    @Id
    private String id= UUID.randomUUID().toString();

    @Column(nullable = false)
    private String choices;

    @Column(nullable = false)
    private Integer votes=0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "poll_id",referencedColumnName = "id")
    private UserModel polls;

    public VotesModel() {
    }

    public VotesModel(String choices, Integer votes) {
        this.choices = choices;
        this.votes = votes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public UserModel getPolls() {
        return polls;
    }

    public void setPolls(UserModel polls) {
        this.polls = polls;
    }
}
