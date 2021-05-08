package com.server.pollingapp.service;

import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.repository.UserRepository;
import com.server.pollingapp.security.PollsUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PollsUserDetailsService implements UserDetailsService {

    final UserRepositoryImpl userRepositoryImpl;

    final UserRepository userRepository;

    @Autowired
    public PollsUserDetailsService(@Lazy UserRepositoryImpl userRepositoryImpl,@Lazy UserRepository userRepository) {
        this.userRepositoryImpl = userRepositoryImpl;
        this.userRepository = userRepository;
    }

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param email the email identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails result;
        if (userRepository.existsByEmail(email)) {
            UserModel userModel = userRepository.findByEmail(email);
            result = new PollsUserDetails(userModel);
        } else {
            throw new UsernameNotFoundException("Email Does Not Exist");
        }
        return result;
    }
}
