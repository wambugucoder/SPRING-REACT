package com.server.pollingapp.schedules;

import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.repository.UserRepository;
import com.server.pollingapp.request.RealTimeLogRequest;
import com.server.pollingapp.service.EmailService;
import com.server.pollingapp.service.JwtService;
import com.server.pollingapp.service.PollStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserSchedule {
    @Autowired
    UserRepository userRepository;

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
        List<UserModel> users = userRepository.findAll();
        for (UserModel eachuser:users) {
            if (!eachuser.getEmailVerificationSent() && eachuser.getAuthProvider().toString().equalsIgnoreCase("local")){
                String token= jwtService.GenerateAccountActivationToken(eachuser.getEmail());
                emailService.createActivationTemplate(token,eachuser);
                //UPDATE EMAILSENT TO TRUE
                eachuser.setEmailVerificationSent(true);
                //GENERATE LOGS
                pollStream.sendToMessageBroker(new RealTimeLogRequest("INFO", eachuser.getEmail()+" "+"Has Received An Email","UserSchedule"));

                userRepository.save(eachuser);

            }
        }

    }
}
