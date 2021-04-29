package com.server.pollingapp.models;


import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "polls")
public class PollModel implements Serializable {
    private static final long serialVersionUID = -4406604081033568663L;

    @Id
    private String id= UUID.randomUUID().toString();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PollsCategory category=PollsCategory.NON_SCHEDULED_POLL;

    @Column(nullable = false)
    private LocalDateTime closingTime;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<VotesModel> votes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private UserModel createdBy;

    @Column(nullable = false)
    private String question;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<ChoiceModel> options ;

    @Column
    private LocalDateTime scheduledTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PollStatus pollStatus;

    @CreatedDate
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;



    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime updateAt;


    public PollModel() {
    }

    public PollModel(PollsCategory category, LocalDateTime closingTime, UserModel createdBy, String question, List<ChoiceModel> options, PollStatus pollStatus) {
        this.category = category;
        this.closingTime = closingTime;
        this.createdBy = createdBy;
        this.question = question;
        this.options = options;
        this.pollStatus = pollStatus;
    }

    public PollModel(PollsCategory category, LocalDateTime closingTime, UserModel createdBy, String question, List<ChoiceModel> options, LocalDateTime scheduledTime, PollStatus pollStatus) {
        this.category = category;
        this.closingTime = closingTime;
        this.createdBy = createdBy;
        this.question = question;
        this.options = options;
        this.scheduledTime = scheduledTime;
        this.pollStatus = pollStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PollsCategory getCategory() {
        return category;
    }

    public void setCategory(PollsCategory category) {
        this.category = category;
    }

    public LocalDateTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalDateTime closingTime) {
        this.closingTime = closingTime;
    }

    public List<VotesModel> getVotes() {
        return votes;
    }

    public void setVotes(List<VotesModel> votes) {
        this.votes = votes;
    }

    public UserModel getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserModel createdBy) {
        this.createdBy = createdBy;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<ChoiceModel> getOptions() {
        return options;
    }

    public void setOptions(List<ChoiceModel> options) {
        this.options = options;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public PollStatus getPollStatus() {
        return pollStatus;
    }

    public void setPollStatus(PollStatus pollStatus) {
        this.pollStatus = pollStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}


