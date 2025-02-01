package ch.cern.todo.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.stream.Stream;

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

    private static final String USERNAME = "user1";
    private static final String PASSWORD = "passwd1";

    private static final MockHttpServletRequestBuilder[] UNAUTHENTICATED_REQUESTS = {
            post("/todo").contentType("application/json"),
            delete("/todo/123").contentType("application/json"),
            get("/todo").contentType("application/json"),
            get("/todo/123").contentType("application/json"),
            put("/todo/123").contentType("application/json"),
            get("/todo/mine").contentType("application/json"),
            post("/todo/mine/search").contentType("application/json"),
            post("/todo/search").contentType("application/json")
    };

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource
    @SneakyThrows
    void testUnauthenticatedRequests(MockHttpServletRequestBuilder request){
        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    static Stream<Arguments> testUnauthenticatedRequests() {
        return Stream.of(UNAUTHENTICATED_REQUESTS).map(Arguments::of);
    }

    @SneakyThrows
    @Test
    void addTodo() {
        this.mockMvc.perform(post("/todo")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void deleteTodo() {
        this.mockMvc.perform(delete("/todo")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void getAllTodo() {
        this.mockMvc.perform(get("/todo")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void getTodoById() {
        this.mockMvc.perform(get("/todo/123")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void updateTodo() {
        this.mockMvc.perform(put("/todo")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void getMyTodos() {
        this.mockMvc.perform(get("/todo/mine")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void searchMyTodos() {
        this.mockMvc.perform(get("/todo/mine/search")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void searchTodos() {
        this.mockMvc.perform(get("/todo/search")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}