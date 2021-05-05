package com.server.pollingapp.service;

import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * The primary reason for using cache is to make data access faster and less expensive.
 * When the highly requested resource is requested multiple times,
 * it is often beneficial for the developer to cache resources so that it can give responses quickly.
 * Using cache in an application enhances the performance of the application.
 * Data access from memory is always faster in comparison to fetching data from the database.
 * It reduces both monetary cost and opportunity cost.
 */
@Service
public class UserRepositoryImpl implements Serializable{

    private static final long serialVersionUID = 2227703948534184774L;
    final UserRepository userRepository;

    @Autowired
    public UserRepositoryImpl(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "Users")
    public List<UserModel> findAllUsers() {
        return this.userRepository.findAll();
    }

    @CacheEvict(value = "Users",allEntries = true)
    public UserModel addUser(UserModel userModel){
        return this.userRepository.save(userModel);
    }

    @CacheEvict(value = "Users",allEntries = true)
    public UserModel updateUser(UserModel userModel){
        return this.userRepository.save(userModel);
    }

    @Cacheable(value = "Users",key = "#id")
    public UserModel findUserById(String id){return this.userRepository.getOne(id);}


}
