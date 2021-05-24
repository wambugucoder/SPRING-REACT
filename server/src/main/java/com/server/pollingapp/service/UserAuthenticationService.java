package com.server.pollingapp.service;

import com.server.pollingapp.models.AuthProvider;
import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.repository.UserRepository;
import com.server.pollingapp.request.LoginRequest;
import com.server.pollingapp.request.RegistrationRequest;
import com.server.pollingapp.response.LoginResponse;
import com.server.pollingapp.response.UniversalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    UserRepositoryImpl userRepositoryImpl;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;
    @Autowired
    EmailService emailService;
    @Autowired
    UserRepository userRepository;

    Logger log= LoggerFactory.getLogger(UserAuthenticationService.class);



    public ResponseEntity<UniversalResponse>RegisterUser(RegistrationRequest registrationRequest){
        // CHECK IF EMAIL OR USERNAME EXISTS
        if (userRepository.existsByEmail(registrationRequest.getEmail())){
            UniversalResponse details = new UniversalResponse();
            details.setMessage("Email Already Exists");
            details.setError(true);

            //GENERATE LOGS
            log.warn("User Already Exists");
            return ResponseEntity.badRequest().body(details);
        }
        else if (userRepository.existsByUsername(registrationRequest.getUsername())){
            UniversalResponse details = new UniversalResponse();
            details.setMessage("UserName Already Exists");
            details.setError(true);

            //GENERATE LOGS
            log.warn(registrationRequest.getUsername()+""+"Already Exists");
            return ResponseEntity.badRequest().body(details);

        }
        //OTHERWISE OF EVERYTHING IS FINE ,ENCRYPT PASSWORD,SAVE USER AND SEND THEM A CONFIRMATION EMAIL
        String password=bCryptPasswordEncoder.encode(registrationRequest.getPassword());

        UserModel newUser=new UserModel(registrationRequest.getUsername(), registrationRequest.getEmail(),password, AuthProvider.local);

        userRepositoryImpl.addUser(newUser);

        //GENERATE LOGS
        log.info("User has just been registered in the system");

        //SEND SUCCESS MESSAGE AFTER REGISTERING USER
        UniversalResponse success=new UniversalResponse();
        success.setMessage("Please Check your Email To Activate your Account");
        success.setError(false);
        return ResponseEntity.ok().body(success);

    }

    public ResponseEntity<LoginResponse> LoginUser(LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                    loginRequest.getPassword()));
        }
        catch (DisabledException e){
            //GENERATE LOGS
            log.warn("User Has not been activated");
            //RETURN 404 ERROR
            LoginResponse loginResponse=new LoginResponse();
            loginResponse.setError(true);
            loginResponse.setMessage("Your Account Has Not been Activated");
            loginResponse.setToken(null);
            return ResponseEntity.badRequest().body(loginResponse);

        }
        catch (LockedException e){
            //GENERATE LOGS
            log.warn("User Has not been activated");
            //RETURN 404 ERROR
            LoginResponse loginResponse=new LoginResponse();
            loginResponse.setError(true);
            loginResponse.setMessage("Your Account Has Not Yet Been Activated");
            loginResponse.setToken(null);
            return ResponseEntity.badRequest().body(loginResponse);

        }
        catch (BadCredentialsException e){
            //GENERATE LOGS
            log.warn("User Has Provided Invalid Credentials");
            //RETURN 404 ERROR
            LoginResponse loginResponse=new LoginResponse();
            loginResponse.setError(true);
            loginResponse.setMessage("Invalid Credentials");
            loginResponse.setToken(null);
            return ResponseEntity.badRequest().body(loginResponse);

        }
        //AFTER SUCCESSFUL AUTHENTICATION CREATE A JWT TOKEN
        UserModel user= userRepository.findByEmail(loginRequest.getEmail());
        String jwtToken=jwtService.GenerateLoginToken(user);
        //GENERATE LOGS

        //RETURN 200 SUCCESS
        LoginResponse loginResponse=new LoginResponse();
        loginResponse.setError(false);
        loginResponse.setMessage("Successfully Logged In");
        loginResponse.setToken("Bearer"+" "+jwtToken);
        return ResponseEntity.ok().body(loginResponse);

    }
    public ResponseEntity<UniversalResponse> ActivateUserAccount(String token){
        //CHECK IF TOKEN IS EXPIRED
        if (!jwtService.ValidateToken(token)){
            UniversalResponse response= new UniversalResponse();
            response.setError(true);
            response.setMessage("Your Activation Token No longer works");
            //GENERATE LOG
            log.warn("Token No Longer Works");
            return ResponseEntity.badRequest().body(response);
        }

        //EXTRACT DETAILS
        String email = jwtService.ExtractEmail(token);
        UserModel user= userRepository.findByEmail(email);
        //CHECK IF USER HAD ALREADY VALIDATED
        if (user.getEnabled()){
            //RETURN ERROR SAYING ACCOUNT WAS ALREADY ACTIVATED
            UniversalResponse response= new UniversalResponse();
            response.setError(true);
            response.setMessage("Your Account was already activated");
            //GENERATE LOG
            log.warn("User Tried to access the activation token Again");
            return ResponseEntity.badRequest().body(response);
        }
        //IF NOT EXPIRED,NOT VALIDATE EXTRACT USER-DETAILS AND SET ENABLED TO TRUE
        user.setEnabled(true);
        user.setAccountNotLocked(true);

        //UPDATE USER CONTENTS
        userRepositoryImpl.updateUser(user);

        //RETURN SUCCESS MESSAGE
        UniversalResponse response= new UniversalResponse();
        response.setError(false);
        response.setMessage("Your Account Has Been Activated Successfully");

        //GENERATE LOG
        log.info("User Has Been Activated Successfully");
        return ResponseEntity.ok().body(response);
    }
}
