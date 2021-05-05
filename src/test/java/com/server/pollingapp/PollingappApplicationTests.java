package com.server.pollingapp;

import com.google.gson.Gson;
import com.server.pollingapp.models.ChoiceModel;
import com.server.pollingapp.models.PollModel;
import com.server.pollingapp.models.PollStatus;
import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.repository.ChoiceRepository;
import com.server.pollingapp.repository.PollRepository;
import com.server.pollingapp.repository.UserRepository;
import com.server.pollingapp.request.*;
import com.server.pollingapp.service.JwtService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = PollingappApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("POLLING-SERVER INTEGRATION TESTS")
class PollingappApplicationTests {

    final MockMvc mockMvc;
    final JwtService jwtService;
    final UserRepository userRepository;
    final PollRepository pollRepository;
    final ChoiceRepository choiceRepository;

    PollingappApplicationTests(MockMvc mockMvc, JwtService jwtService, UserRepository userRepository, PollRepository pollRepository, ChoiceRepository choiceRepository) {
        this.mockMvc = mockMvc;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
        this.choiceRepository = choiceRepository;
    }

    /**
     * Registration Tests from order 1- order 4
     * @throws Exception
     */

    @Test
    @Order(1)
    @DisplayName("/api/v1/auth/signup - Given Correct Details")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
   // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void RegisterUserSuccessfully() throws Exception {
        //GIVEN REGISTRATION DETAILS
        Gson gson=new Gson();
        RegistrationRequest registrationRequest=new RegistrationRequest("abcdef","abcd@gmail.com","123456");
        String jsonData = gson.toJson(registrationRequest);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/signup").secure(true).content(jsonData).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
        .andExpect(ResultMatcher.matchAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.error").value(false)
        ));
    }
    @Test
    @Order(2)
    @DisplayName("/api/v1/auth/signup - Given Correct Details")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void RegisterUnActivatedUser() throws Exception {
        //GIVEN REGISTRATION DETAILS
        Gson gson=new Gson();
        RegistrationRequest registrationRequest=new RegistrationRequest("notactivated","notactivated@gmail.com","123456");
        String jsonData = gson.toJson(registrationRequest);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/signup").secure(true).content(jsonData).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.error").value(false)
                ));
    }


    @Test
    @Order(3)
    @DisplayName("/api/v1/auth/signup - Given Wrong Email")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void DoNotRegisterUserSuccessfully() throws Exception {
        //GIVEN A WRONG EMAIL
        Gson gson=new Gson();
        RegistrationRequest registrationRequest=new RegistrationRequest("abcdef","abc","123456");
        String jsonData = gson.toJson(registrationRequest);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/signup").secure(true).content(jsonData).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().is4xxClientError(),
                        MockMvcResultMatchers.jsonPath("$.error").value(true)
                ));
    }
    @Test
    @Order(4)
    @DisplayName("/api/v1/auth/signup - Given Already Existing Email")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void GivenAlreadyExistingEmail() throws Exception {
        //GIVEN REGISTRATION DETAILS
        Gson gson=new Gson();
        RegistrationRequest registrationRequest=new RegistrationRequest("abcdef","abcd@gmail.com","123456");
        String jsonData = gson.toJson(registrationRequest);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/signup").secure(true).content(jsonData).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().is4xxClientError(),
                        MockMvcResultMatchers.jsonPath("$.error").value(true)
                ));
    }


    /**
    * Token Validation Tests order 5- order 7
    */

    @Test
    @Order(5)
    @DisplayName("/api/v1/auth/activate/:token - Given Correct Token")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void SuccessfullyValidateToken() throws Exception {
        //GIVEN A VALID TOKEN
        String validToken = jwtService.GenerateAccountActivationToken("abcd@gmail.com");


        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/auth/activate/"+validToken).secure(true)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.error").value(false)
                ));
    }
    @Test
    @Order(6)
    @DisplayName("/api/v1/auth/activate/:token - Given No Token")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void DoNotValidateEmpty() throws Exception {
        //GIVEN NO TOKEN
        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/auth/activate/").secure(true)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().is4xxClientError(),
                        MockMvcResultMatchers.jsonPath("$.error").value(true)
                ));
    }
    @Test
    @Order(7)
    @DisplayName("/api/v1/auth/activate/:token - Given Invalid Signature Token")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void DoNotValidateInvalidSignatureToken() throws Exception {
        //GIVEN A VALID TOKEN
        String InvalidToken ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c" ;


        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/auth/activate/"+InvalidToken).secure(true)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().is4xxClientError(),
                        MockMvcResultMatchers.jsonPath("$.error").value(true)
                ));
    }

    /**
     * Login Validation Tests order 8- order 11
     */

    @Test
    @Order(8)
    @DisplayName("/api/v1/auth/signin - Given Correct Login Details")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void LoginUserWithCorrectDetails() throws Exception {
        //GIVEN CORRECT LOGIN DETAILS
        Gson gson=new Gson();
        LoginRequest loginRequest=new LoginRequest("abcd@gmail.com","123456");
        String jsonData=gson.toJson(loginRequest);


        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/signin").secure(true).content(jsonData).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.error").value(false),
                        MockMvcResultMatchers.jsonPath("$.token").exists()
                ));
    }
    @Test
    @Order(9)
    @DisplayName("/api/v1/auth/signin - Given Wrong Email")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void DoNotLoginWithWrongEmail() throws Exception {
        //GIVEN CORRECT LOGIN DETAILS
        Gson gson=new Gson();
        LoginRequest loginRequest=new LoginRequest("abc@gmail.com","123456");
        String jsonData=gson.toJson(loginRequest);


        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/signin").secure(true).content(jsonData).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().is4xxClientError(),
                        MockMvcResultMatchers.jsonPath("$.error").value(true)
                ));
    }
    @Test
    @Order(10)
    @DisplayName("/api/v1/auth/signin - Given Wrong Password")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void DoNotLoginWithWrongPassword() throws Exception {
        //GIVEN CORRECT LOGIN DETAILS
        Gson gson=new Gson();
        LoginRequest loginRequest=new LoginRequest("abcd@gmail.com","12345");
        String jsonData=gson.toJson(loginRequest);


        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/signin").secure(true).content(jsonData).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().is4xxClientError(),
                        MockMvcResultMatchers.jsonPath("$.error").value(true)
                ));
    }
    @Test
    @Order(11)
    @DisplayName("/api/v1/auth/signin - Given UnActivated Credentials")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void DoNotLoginWithUnActivatedCreds() throws Exception {
        //GIVEN CORRECT LOGIN DETAILS
        Gson gson=new Gson();
        LoginRequest loginRequest=new LoginRequest("notactivated@gmail.com","123456");
        String jsonData=gson.toJson(loginRequest);


        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/signin").secure(true).content(jsonData).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().is4xxClientError(),
                        MockMvcResultMatchers.jsonPath("$.error").value(true)
                ));
    }
    /**
     * Polls Test order 12->order 20
     */
    @Test
    @Order(12)
    @DisplayName("/api/v1/polls/{userId}/scheduled_poll - Create A scheduled Poll")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void CreateAScheduledPoll() throws Exception {
        //GIVEN USERID
        UserModel user=userRepository.findByEmail("abcd@gmail.com");
        String id= user.getId();

        //GIVEN AUTH TOKEN
        String token=jwtService.GenerateLoginToken(user);

        //GIVEN SCHEDULED POLL
        List<ChoiceRequest> list= new ArrayList<>();
        list.add(new ChoiceRequest("A"));
        list.add(new ChoiceRequest("B"));
        ScheduledPollRequest scheduledPollRequest=new ScheduledPollRequest();
        scheduledPollRequest.setQuestion("A or B ?");
        scheduledPollRequest.setOptions(list);
        scheduledPollRequest.setScheduledTime(LocalDateTime.now().plusHours(1));
        scheduledPollRequest.setClosingTime(LocalDateTime.now().plusHours(2));
        //CONVERT TO JSON
        Gson gson=new Gson();
        String jsonData=gson.toJson(scheduledPollRequest);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/polls/"+id+"/scheduled_poll")
                        .secure(true)
                        .header("Authorization","Bearer"+" "+token)
                        .content(jsonData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
        .andExpect(ResultMatcher.matchAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.error").value(false)

        ));

    }
    @Test
    @Order(13)
    @DisplayName("/api/v1/polls/{userId}/non_scheduled_poll - Create A Non Scheduled Poll")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void CreateANonScheduledPoll() throws Exception {
        //GIVEN USERID
        UserModel user=userRepository.findByEmail("abcd@gmail.com");
        String id= user.getId();

        //GIVEN AUTH TOKEN
        String token=jwtService.GenerateLoginToken(user);

        //GIVEN SCHEDULED POLL

        List<ChoiceRequest> list= new ArrayList<>();
        list.add(new ChoiceRequest("Java"));
        list.add(new ChoiceRequest("JavaScript"));
        NonScheduledPollRequest nonscheduledPollRequest=new NonScheduledPollRequest();
        nonscheduledPollRequest.setQuestion("Java or JavaScript?");
        nonscheduledPollRequest.setOptions(list);
        nonscheduledPollRequest.setClosingTime(LocalDateTime.now().plusMinutes(1));
        //CONVERT TO JSON
        Gson gson=new Gson();
        String jsonData=gson.toJson(nonscheduledPollRequest);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/polls/"+id+"/non_scheduled_poll")
                        .secure(true)
                        .header("Authorization","Bearer"+" "+token)
                        .content(jsonData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.error").value(false)

                ));

    }
    @Test
    @Order(14)
    @DisplayName("/api/v1/polls/{userId}/non_scheduled_poll - Do Not Create A Poll with One Option")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void DoNotCreateANonScheduledWithOneOption() throws Exception {
        //GIVEN USERID
        UserModel user=userRepository.findByEmail("abcd@gmail.com");
        String id= user.getId();

        //GIVEN AUTH TOKEN
        String token=jwtService.GenerateLoginToken(user);
        //GIVEN SCHEDULED POLL

        List<ChoiceRequest> list= new ArrayList<>();
        list.add(new ChoiceRequest("JavaScript"));
        NonScheduledPollRequest nonscheduledPollRequest=new NonScheduledPollRequest();
        nonscheduledPollRequest.setQuestion("Java or JavaScript?");
        nonscheduledPollRequest.setOptions(list);
        nonscheduledPollRequest.setClosingTime(LocalDateTime.now().plusMinutes(1));
        //CONVERT TO JSON
        Gson gson=new Gson();
        String jsonData=gson.toJson(nonscheduledPollRequest);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/polls/"+id+"/non_scheduled_poll")
                        .secure(true)
                        .header("Authorization","Bearer"+" "+token)
                        .content(jsonData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().is4xxClientError(),
                        MockMvcResultMatchers.jsonPath("$.error").value(true)

                ));

    }
    @Test
    @Order(15)
    @DisplayName("/api/v1/polls/{userId}/scheduled_poll - Do Not Create A Scheduled Poll with one option")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void DoNotCreateAScheduledPollwithOneOption() throws Exception {
        //GIVEN USERID
        UserModel user=userRepository.findByEmail("abcd@gmail.com");
        String id= user.getId();

        //GIVEN AUTH TOKEN
        String token=jwtService.GenerateLoginToken(user);

        //GIVEN SCHEDULED POLL

        List<ChoiceRequest> list= new ArrayList<>();
        list.add(new ChoiceRequest("B"));
        ScheduledPollRequest scheduledPollRequest=new ScheduledPollRequest();
        scheduledPollRequest.setQuestion("A or B ?");
        scheduledPollRequest.setOptions(list);
        scheduledPollRequest.setScheduledTime(LocalDateTime.now().plusHours(1));
        scheduledPollRequest.setClosingTime(LocalDateTime.now().plusHours(2));
        //CONVERT TO JSON
        Gson gson=new Gson();
        String jsonData=gson.toJson(scheduledPollRequest);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/polls/"+id+"/scheduled_poll")
                        .secure(true)
                        .header("Authorization","Bearer"+" "+token)
                        .content(jsonData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.error").value(false)

                ));

    }
    @Test
    @Order(16)
    @DisplayName("/api/v1/polls/{userId}/scheduled_poll -Do Not Publish Polls that violates code of conduct")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void CaptureDeMeaningPolls() throws Exception {
        //GIVEN USERID
        UserModel user=userRepository.findByEmail("abcd@gmail.com");
        String id= user.getId();

        //GIVEN AUTH TOKEN
        String token=jwtService.GenerateLoginToken(user);

        //GIVEN SCHEDULED POLL

        List<ChoiceRequest> list= new ArrayList<>();
        list.add(new ChoiceRequest("Yes"));
        list.add(new ChoiceRequest("No"));
        ScheduledPollRequest scheduledPollRequest=new ScheduledPollRequest();
        scheduledPollRequest.setQuestion(" Are you racist?");
        scheduledPollRequest.setOptions(list);
        scheduledPollRequest.setScheduledTime(LocalDateTime.now().plusHours(1));
        scheduledPollRequest.setClosingTime(LocalDateTime.now().plusHours(2));
        //CONVERT TO JSON
        Gson gson=new Gson();
        String jsonData=gson.toJson(scheduledPollRequest);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/polls/"+id+"/scheduled_poll")
                        .secure(true)
                        .header("Authorization","Bearer"+" "+token)
                        .content(jsonData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.error").value(false)

                ));

    }
    @Test
    @Order(17)
    @DisplayName("/api/v1/polls/opened_polls -Retrieve All Open Polls")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void RetrieveAllOpenPolls() throws Exception {
        //GIVEN USERDETAILS
        UserModel user=userRepository.findByEmail("abcd@gmail.com");

        //GIVEN AUTH TOKEN
        String token=jwtService.GenerateLoginToken(user);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/polls/opened_polls")
                        .secure(true)
                        .header("Authorization","Bearer"+" "+token)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().isOk()

                ));

    }
    @Test
    @Order(18)
    @DisplayName("/api/v1/polls/closed_polls -Retrieve All Closed Polls")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void RetrieveAllClosedPolls() throws Exception {
        //GIVEN USERDETAILS
        UserModel user=userRepository.findByEmail("abcd@gmail.com");

        //GIVEN AUTH TOKEN
        String token=jwtService.GenerateLoginToken(user);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/polls/closed_polls")
                        .secure(true)
                        .header("Authorization","Bearer"+" "+token)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().isOk()


                ));

    }
    @Test
    @Order(19)
    @DisplayName("/api/v1/polls/scheduled_polls -Retrieve All Scheduled Polls")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void RetrieveAllScheduledPolls() throws Exception {
        //GIVEN USERDETAILS
        UserModel user=userRepository.findByEmail("abcd@gmail.com");

        //GIVEN AUTH TOKEN
        String token=jwtService.GenerateLoginToken(user);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/polls/scheduled_polls")
                        .secure(true)
                        .header("Authorization","Bearer"+" "+token)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().isOk()

                ));

    }
    @Test
    @Order(20)
    @DisplayName("/api/v1/polls/specific_poll/{pollId} -Retrieve All Open Polls")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void RetrieveSpecificPoll() throws Exception {
        //GIVEN USERDETAILS
        UserModel user=userRepository.findByEmail("abcd@gmail.com");
        PollModel poll=pollRepository.findAll().get(0);

        //GIVEN AUTH TOKEN
        String token=jwtService.GenerateLoginToken(user);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/polls/specific_poll/"+poll.getId())
                        .secure(true)
                        .header("Authorization","Bearer"+" "+token)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().isOk()
                ));

    }

    /**
     * Vote Test order 21-> 22
     * @throws Exception
     */
    @Test
    @Order(21)
    @DisplayName("/api/v1/polls/cast_vote/{userId}/{pollId}/{choiceId} -Cast Vote")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void CastVote() throws Exception {
        //GIVEN USERDETAILS
        UserModel user=userRepository.findByEmail("abcd@gmail.com");
        PollModel poll=pollRepository.findAllByPollStatusEquals(PollStatus.POLL_OPENED).get(0);
        ChoiceModel choiceModel=poll.getOptions().get(0);

        //GIVEN AUTH TOKEN
        String token=jwtService.GenerateLoginToken(user);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/polls/cast_vote/"+user.getId()+"/"+poll.getId()+"/"+choiceModel.getId())
                        .secure(true)
                        .header("Authorization","Bearer"+" "+token)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().isOk()

                ));
    }
    @Test
    @Order(22)
    @DisplayName("/api/v1/polls/cast_vote/{userId}/{pollId}/{choiceId} -Cast Vote")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void DoNotCastVoteTwice() throws Exception {
        //GIVEN USERDETAILS
        UserModel user=userRepository.findByEmail("abcd@gmail.com");
        PollModel poll=pollRepository.findAllByPollStatusEquals(PollStatus.POLL_OPENED).get(0);
        ChoiceModel choiceModel=poll.getOptions().get(1);

        //GIVEN AUTH TOKEN
        String token=jwtService.GenerateLoginToken(user);

        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/polls/cast_vote/"+user.getId()+"/"+poll.getId()+"/"+choiceModel.getId())
                        .secure(true)
                        .header("Authorization","Bearer"+" "+token)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().is4xxClientError()
                ));
    }

    /**
     * UnAuthTest order 23
     * @throws Exception
     */
    @Test
    @Order(23)
    @DisplayName("/api/v1/unauthorized/auth -Redirect To Unauthorized")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void RedirectToUnAuthorized() throws Exception {
        //GIVEN NOTHING
        //WHEN API IS CALLED
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/unauthorized/auth")
                        .secure(true)
                        .accept(MediaType.APPLICATION_JSON)
        )
                //EXPECT THE FOLLOWING
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().is4xxClientError()
                ));

    }
}
