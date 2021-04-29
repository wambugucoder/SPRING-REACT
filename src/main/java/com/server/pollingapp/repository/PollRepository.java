package com.server.pollingapp.repository;

import com.server.pollingapp.models.PollModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<PollModel,String> {
}
