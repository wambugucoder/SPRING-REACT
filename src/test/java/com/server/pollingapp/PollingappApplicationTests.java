package com.server.pollingapp;

import com.google.gson.Gson;
import com.server.pollingapp.request.RegistrationRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
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

    PollingappApplicationTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @Order(1)
    @DisplayName("/api/v1/auth/signup  -Given Correct Details")
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
    @DisplayName("/api/v1/auth/signup  -Given Wrong Email")
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


}
