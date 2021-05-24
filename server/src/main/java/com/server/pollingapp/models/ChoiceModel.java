package com.server.pollingapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "choices")
@Proxy(lazy=false)
public class ChoiceModel implements Serializable {

    private static final long serialVersionUID = 5952008960623028980L;
    @Id
    private String id= UUID.randomUUID().toString();

    @Column(nullable = false)
    private String option;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
   // @Fetch(value = FetchMode.SUBSELECT)
    private List<VotesModel> incomingvotes;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
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

    public List<VotesModel> getIncomingvotes() {
        return incomingvotes;
    }

    public void setIncomingvotes(List<VotesModel> votes) {
        this.incomingvotes = votes;
    }
   public void addVotes(VotesModel votes){
        incomingvotes.add(votes);
        setIncomingvotes(incomingvotes);
   }
    public PollModel getPolls() {
        return polls;
    }

    public void setPolls(PollModel polls) {
        this.polls = polls;
    }
}
