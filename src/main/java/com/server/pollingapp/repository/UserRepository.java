package com.server.pollingapp.repository;

import com.server.pollingapp.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



/**
 * @author Jos Wambugu
 * @since 13-04-2021
 */
@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    UserModel findByEmail(String username);


}
