package com.server.pollingapp.repository;

import com.server.pollingapp.models.VotesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotesRepository extends JpaRepository<VotesModel,String> {
}
