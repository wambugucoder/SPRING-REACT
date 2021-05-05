package com.server.pollingapp;

import com.google.gson.Gson;
import com.server.pollingapp.request.RegistrationRequest;
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

    PollingappApplicationTests(MockMvc mockMvc, JwtService jwtService) {
        this.mockMvc = mockMvc;
        this.jwtService = jwtService;
    }

    /**
     * Registration Tests from order 1- order3
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
    @Order(3)
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
    * Token Validation Tests order 4- order 6
    */

    @Test
    @Order(4)
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
    @Order(5)
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
    @Order(6)
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
     * Login Validation Tests order 7- order 10
     */
    @Test
    @Order(7)
    @DisplayName("/api/v1/auth/signin - Given Correct Login Details")
    @EnabledOnJre(value = JRE.JAVA_8,disabledReason = "Server Was Programmed to run on Java 8 Environment")
    // @EnabledOnOs(value=OS.LINUX,disabledReason = "Test should run under docker in a CI/CD environment")
    public void LoginUserWithCorrectDetails() throws Exception {
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


}
