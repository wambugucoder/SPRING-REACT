package com.server.pollingapp.repository;

import com.server.pollingapp.models.VotesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotesRepository extends JpaRepository<VotesModel,String> {
}
