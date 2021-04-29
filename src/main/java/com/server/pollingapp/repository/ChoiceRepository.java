package com.server.pollingapp.repository;

import com.server.pollingapp.models.ChoiceModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoiceRepository extends JpaRepository<ChoiceModel,String> {
}
