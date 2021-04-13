package com.server.pollingapp.repository;

import com.server.pollingapp.models.AddressModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
/**
 * @author Jos Wambugu
 * @since 13-04-2021
 */
@Repository
public interface AddressRepository extends JpaRepository<AddressModel, UUID> {
}
