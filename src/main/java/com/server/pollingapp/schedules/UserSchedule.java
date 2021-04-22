package com.server.pollingapp.schedules;

import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.repository.UserRepository;
import com.server.pollingapp.request.RealTimeLogRequest;
import com.server.pollingapp.service.EmailService;
import com.server.pollingapp.service.JwtService;
import com.server.pollingapp.service.PollStream;
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

    @Autowired
    PollStream pollStream;

    /**
     * This scheduled method is supposed to run after every 1 minute sending verfication emails to users
     * who havent received the activation token and who also registered using local Auth Provider.
     * I noticed response time was slow in the register api due to emails being sent per user
     * So i decided to run that task in bulk while in background.
     */
    @Scheduled(fixedDelay = 60000)
    public void sendEmailVerification(){
        List<UserModel> users = userRepositoryImpl.findAllUsers();
        if(!users.isEmpty()){
            users.stream().filter(eachuser -> (!eachuser.getEmailVerificationSent() && eachuser.getAuthProvider().toString().equalsIgnoreCase("local"))).map((UserModel eachuser) -> {
                String token= jwtService.GenerateAccountActivationToken(eachuser.getEmail());
                emailService.createActivationTemplate(token,eachuser);
                return eachuser;
            }).map(eachuser -> {
                //UPDATE EMAILSENT TO TRUE
                eachuser.setEmailVerificationSent(true);
                return eachuser;
            }).map(eachuser -> {
                //GENERATE LOGS
                pollStream.sendToMessageBroker(new RealTimeLogRequest("INFO", eachuser.getEmail()+" "+"Has Received An Email","UserSchedule"));
                return eachuser;
            }).forEachOrdered(eachuser -> {
                userRepositoryImpl.updateUser(eachuser);
            });

        }

    }
}
