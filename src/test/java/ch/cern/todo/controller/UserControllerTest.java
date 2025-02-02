package ch.cern.todo.controller;

import ch.cern.todo.openapi.model.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/setup_test_database.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/add_users.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/tear_down_database.sql")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void createUser() {
        this.mockMvc.perform(post("/user/register")
                        .content(objectMapper.writeValueAsString(getCreateUserRequest()))
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("new_user")))
                .andExpect(jsonPath("$.role", is("USER")));
    }

    @SneakyThrows
    @Test
    void createTwiceSameUser() {
        this.mockMvc.perform(post("/user/register")
                .content(objectMapper.writeValueAsString(getCreateUserRequest()))
                .contentType("application/json"));

        this.mockMvc.perform(post("/user/register")
                        .content(objectMapper.writeValueAsString(getCreateUserRequest()))
                        .contentType("application/json"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("User new_user already exists")))
                .andExpect(jsonPath("$.code", is("USER_ALREADY_EXISTS")));
    }

    private static CreateUserRequest getCreateUserRequest() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("new_user");
        createUserRequest.setPassword("password");
        return createUserRequest;
    }

}