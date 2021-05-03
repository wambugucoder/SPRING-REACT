package com.server.pollingapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "choices")
public class ChoiceModel implements Serializable {

    private static final long serialVersionUID = 5952008960623028980L;
    @Id
    private String id= UUID.randomUUID().toString();

    @Column(nullable = false)
    private String option;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<VotesModel> votes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id",referencedColumnName = "id")
    @JsonBackReference
    private PollModel polls;

    public ChoiceModel() {
    }

    public ChoiceModel(String option, PollModel polls) {
        this.option = option;
        this.polls = polls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String choices) {
        this.option = choices;
    }

    public List<VotesModel> getVotes() {
        return votes;
    }

    public void setVotes(List<VotesModel> votes) {
        this.votes = votes;
    }

    public PollModel getPolls() {
        return polls;
    }

    public void setPolls(PollModel polls) {
        this.polls = polls;
    }
}
