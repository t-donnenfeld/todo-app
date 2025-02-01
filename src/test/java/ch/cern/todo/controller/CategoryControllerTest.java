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

@Sql("/scripts/setup_test_database.sql")
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void addCategory() {
        this.mockMvc.perform(post("/category")
                        .contentType("application/json")
                        .with(httpBasic("user1", "passwd1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void deleteCategory() {
        this.mockMvc.perform(delete("/category")
                        .contentType("application/json")
                        .with(httpBasic("user1", "passwd1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void getAllCategory() {
        this.mockMvc.perform(get("/category")
                        .contentType("application/json")
                        .with(httpBasic("user1", "passwd1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void getCategoryById() {
        this.mockMvc.perform(get("/category/123")
                        .contentType("application/json")
                        .with(httpBasic("user1", "passwd1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void updateCategory() {
        this.mockMvc.perform(put("/category")
                        .contentType("application/json")
                        .with(httpBasic("user1", "passwd1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}