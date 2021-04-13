package com.server.pollingapp.service;

import com.server.pollingapp.models.AddressModel;
import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.repository.UserRepository;
import com.server.pollingapp.request.RegistrationRequest;
import com.server.pollingapp.response.RegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


/**
 * @author Jos Wambugu
 * @since 13-04-2021
 * @apiNote <p>
 *     <b> The UserAuthenticationService should only contain RegisterUser(signup),LoginUser(signin) and validate access token methods).</b>
 *
 *     <li>RegisterUser-> Collects User Details,Validates them,Encrypts password,Saves details to db and sends an email to activate account</li>
 *     <li>LoginUser->Collects Login Credentials,if credentials are correct it generates a jwtToken for session Mgmt</li>
 *     <li>ValidateAccount-> Once Registered an Email is Sent containing a link,
 *                         link contains token which should be validated.This is to verify that the account user is real.
 *      </li>
 *
 * </p>
 */
@Service
public class UserAuthenticationService {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<RegistrationResponse>RegisterUser(RegistrationRequest registrationRequest){
        // CHECK IF EMAIL OR USERNAME EXISTS
        if (userRepository.existsByEmail(registrationRequest.getEmail())){
            RegistrationResponse details = new RegistrationResponse();
            details.setMessage("Email Already Exists");
            details.setError(true);
            return ResponseEntity.badRequest().body(details);
        }
        else if (userRepository.existsByUsername(registrationRequest.getUsername())){
            RegistrationResponse details = new RegistrationResponse();
            details.setMessage("UserName Already Exists");
            details.setError(true);
            return ResponseEntity.badRequest().body(details);

        }
        //OTHERWISE OF EVERYTHING IS FINE SAVE USER AND SEND THEM A CONFIRMATION EMAIL
        AddressModel addressModel=new AddressModel(registrationRequest.getCity(), registrationRequest.getCountry());
        UserModel newUser=new UserModel(registrationRequest.getUsername(), registrationRequest.getEmail(),
                         registrationRequest.getPassword(),addressModel );

        userRepository.save(newUser);

        //SEND SUCCESS MESSAGE AFTER REGISTERING USER
        RegistrationResponse success=new RegistrationResponse();
        success.setMessage("Please Check your Email To Activate your Account");
        success.setError(false);
        return ResponseEntity.ok().body(success);




    }
}
