package ch.cern.todo.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/setup_test_database.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/tear_down_database.sql")
@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void addTodo() {
        this.mockMvc.perform(post("/todo")
                        .contentType("application/json")
                        .with(httpBasic("user1", "passwd1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void deleteTodo() {
        this.mockMvc.perform(delete("/todo")
                        .contentType("application/json")
                        .with(httpBasic("user1", "passwd1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void getAllTodo() {
        this.mockMvc.perform(get("/todo")
                        .contentType("application/json")
                        .with(httpBasic("user1", "passwd1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void getTodoById() {
        this.mockMvc.perform(get("/todo/123")
                        .contentType("application/json")
                        .with(httpBasic("user1", "passwd1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void updateTodo() {
        this.mockMvc.perform(put("/todo")
                        .contentType("application/json")
                        .with(httpBasic("user1", "passwd1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void getMyTodos() {
        this.mockMvc.perform(get("/todo/mine")
                        .contentType("application/json")
                        .with(httpBasic("user1", "passwd1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void searchMyTodos() {
        this.mockMvc.perform(get("/todo/mine/search")
                        .contentType("application/json")
                        .with(httpBasic("user1", "passwd1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void searchTodos() {
        this.mockMvc.perform(get("/todo/search")
                        .contentType("application/json")
                        .with(httpBasic("user1", "passwd1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}