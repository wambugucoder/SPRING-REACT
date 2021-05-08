package com.server.pollingapp.repository;

import com.server.pollingapp.models.UserModel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;


/**
 * @author Jos Wambugu
 * @since 13-04-2021
 */
@Repository
public interface UserRepository extends JpaRepository<UserModel, String> , Serializable {
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    @Cacheable(value = "Users", key = "#email")
    UserModel findByEmail(String email) ;





}
