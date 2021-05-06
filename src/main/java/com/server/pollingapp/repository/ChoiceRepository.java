package com.server.pollingapp.repository;

import com.server.pollingapp.models.ChoiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoiceRepository extends JpaRepository<ChoiceModel,String> {

    ChoiceModel findByOption(String option);
}
