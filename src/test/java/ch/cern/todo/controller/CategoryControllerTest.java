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
class CategoryControllerTest {

    private static final String USERNAME = "user1";
    private static final String PASSWORD = "passwd1";

    private static final MockHttpServletRequestBuilder[] UNAUTHENTICATED_REQUESTS = {
            post("/category").contentType("application/json"),
            delete("/category").contentType("application/json"),
            get("/category").contentType("application/json"),
            put("/category/123").contentType("application/json")
    };

    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void getAllCategory() {
        this.mockMvc.perform(get("/category")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

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
    void addCategory() {
        this.mockMvc.perform(post("/category")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void addCategoryUnauthenticated() {
        this.mockMvc.perform(post("/category")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @SneakyThrows
    @Test
    void deleteCategory() {
        this.mockMvc.perform(delete("/category")
                        .contentType("application/json")
                       .with(httpBasic(USERNAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void updateCategory() {
        this.mockMvc.perform(put("/category/123")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}