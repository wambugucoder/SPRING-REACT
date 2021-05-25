package com.server.pollingapp.schedules;

import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.service.EmailService;
import com.server.pollingapp.service.JwtService;
import com.server.pollingapp.service.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserSchedule {
    @Autowired
    UserRepositoryImpl userRepositoryImpl;

    @Autowired
    JwtService jwtService;

    @Autowired
    EmailService emailService;



    /**
     * This scheduled method is supposed to run after every 1 minute sending verification emails to users
     * who haven't received the activation token and who also registered using local Auth Provider.
     * I noticed response time was slow in the register api due to emails being sent per user
     * So i decided to run that task in bulk while in background.
     */
    @Scheduled(fixedDelay = 60000)
    private void sendEmailVerification(){
        List<UserModel> users = userRepositoryImpl.findAllUsers();
        if(!users.isEmpty()){
            users.stream().filter(eachuser -> (!eachuser.getEmailVerificationSent() && eachuser.getAuthProvider().toString().equalsIgnoreCase("local"))).map((UserModel eachuser) -> {
                String token= jwtService.GenerateAccountActivationToken(eachuser.getEmail());
                emailService.createActivationTemplate(token,eachuser);
                return eachuser;
            }).peek(eachuser -> {
                //UPDATE EMAILSENT TO TRUE
                eachuser.setEmailVerificationSent(true);
            }).peek(eachuser -> {
                //GENERATE LOGS

            }).forEachOrdered(userRepositoryImpl::updateUser);

        }

    }
}
