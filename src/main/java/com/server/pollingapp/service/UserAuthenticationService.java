package com.server.pollingapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.pollingapp.models.AddressModel;
import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.repository.UserRepository;
import com.server.pollingapp.request.LoginRequest;
import com.server.pollingapp.request.RealTimeLogRequest;
import com.server.pollingapp.request.RegistrationRequest;
import com.server.pollingapp.response.LoginResponse;
import com.server.pollingapp.response.RegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * @author Jos Wambugu
 * @since 13-04-2021
 * @apiNote <p>
 *     <b> The UserAuthenticationService should only contain RegisterUser(signup),LoginUser(signIn) and validate access token methods).</b>
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
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;
    @Autowired
    PollStream pollStream;
    @Autowired
    EmailService emailService;

    public ResponseEntity<RegistrationResponse>RegisterUser(RegistrationRequest registrationRequest){
        // CHECK IF EMAIL OR USERNAME EXISTS
        if (userRepository.existsByEmail(registrationRequest.getEmail())){
            RegistrationResponse details = new RegistrationResponse();
            details.setMessage("Email Already Exists");
            details.setError(true);

            //GENERATE LOGS
            pollStream.sendToMessageBroker(new RealTimeLogRequest("WARN","Someone tried Registering" +
                    "With an Email That Already exists","UserAuthenticationService"));
            return ResponseEntity.badRequest().body(details);
        }
        else if (userRepository.existsByUsername(registrationRequest.getUsername())){
            RegistrationResponse details = new RegistrationResponse();
            details.setMessage("UserName Already Exists");
            details.setError(true);

            //GENERATE LOGS
            pollStream.sendToMessageBroker(new RealTimeLogRequest("WARN","Someone tried Registering" +
                    "With a UserName That Already exists","UserAuthenticationService"));
            return ResponseEntity.badRequest().body(details);

        }
        //OTHERWISE OF EVERYTHING IS FINE ,ENCRYPT PASSWORD,SAVE USER AND SEND THEM A CONFIRMATION EMAIL
        String password=bCryptPasswordEncoder.encode(registrationRequest.getPassword());

        AddressModel addressModel=new AddressModel(registrationRequest.getCity(), registrationRequest.getCountry());
        UserModel newUser=new UserModel(registrationRequest.getUsername(), registrationRequest.getEmail(),
                         password,addressModel );

        userRepository.save(newUser);

        //GENERATE LOGS
        pollStream.sendToMessageBroker(new RealTimeLogRequest("INFO", registrationRequest.getUsername()+" Has Successfully Been Registered","UserAuthenticationService"));


        //CREATE ACTIVATION TOKEN
        String activationToken=jwtService.GenerateAccountActivationToken(registrationRequest.getUsername());

        //SEND EMAIL WITH LINK->TODO
        emailService.createActivationTemplate(activationToken,registrationRequest);

        //GENERATE LOGS
        pollStream.sendToMessageBroker(new RealTimeLogRequest("INFO", registrationRequest.getUsername()+" Has Received An Email","UserAuthenticationService"));

        //SEND SUCCESS MESSAGE AFTER REGISTERING USER
        RegistrationResponse success=new RegistrationResponse();
        success.setMessage("Please Check your Email To Activate your Account");
        success.setError(false);
        return ResponseEntity.ok().body(success);

    }

    public ResponseEntity<LoginResponse> LoginUser(LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                    loginRequest.getPassword()));
        }
        catch (DisabledException e){
            //GENERATE LOGS
            pollStream.sendToMessageBroker(new RealTimeLogRequest("WARN", loginRequest.getUsername()+"Tried To Login with a Disabled Account","UserAuthenticationService"));
            //RETURN 404 ERROR
            LoginResponse loginResponse=new LoginResponse();
            loginResponse.setError(true);
            loginResponse.setMessage("Your Account Has Not been Activated");
            loginResponse.setToken(null);
            return ResponseEntity.badRequest().body(loginResponse);

        }
        catch (LockedException e){
            //GENERATE LOGS
            pollStream.sendToMessageBroker(new RealTimeLogRequest("WARN", loginRequest.getUsername()+"Tried To Login with a Disabled Account","UserAuthenticationService"));
            //RETURN 404 ERROR
            LoginResponse loginResponse=new LoginResponse();
            loginResponse.setError(true);
            loginResponse.setMessage("Your Account Has Not Yeet Been Activated");
            loginResponse.setToken(null);
            return ResponseEntity.badRequest().body(loginResponse);

        }
        catch (BadCredentialsException e){
            //GENERATE LOGS
            pollStream.sendToMessageBroker(new RealTimeLogRequest("WARN", loginRequest.getUsername()+"Submitted Invalid Login Credentials","UserAuthenticationService"));
            //RETURN 404 ERROR
            LoginResponse loginResponse=new LoginResponse();
            loginResponse.setError(true);
            loginResponse.setMessage("Invalid Credentials");
            loginResponse.setToken(null);
            return ResponseEntity.badRequest().body(loginResponse);

        }
        //AFTER SUCCESSFUL AUTHENTICATION CREATE A JWT TOKEN
        UserModel user= userRepository.findByUsername(loginRequest.getUsername());
        String jwtToken=jwtService.GenerateLoginToken(user);
        //GENERATE LOGS
        pollStream.sendToMessageBroker(new RealTimeLogRequest("INFO", loginRequest.getUsername()+"Successfully Logged In","UserAuthenticationService"));
        //RETURN 200 ERROR
        LoginResponse loginResponse=new LoginResponse();
        loginResponse.setError(false);
        loginResponse.setMessage("Successfully Logged In");
        loginResponse.setToken("Bearer" +jwtToken);
        return ResponseEntity.ok().body(loginResponse);

    }
}
