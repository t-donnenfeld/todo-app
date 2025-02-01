package ch.cern.todo.controller;

import ch.cern.todo.openapi.model.AddCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/setup_test_database.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/add_category.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/tear_down_database.sql")
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    private static final String USERNAME = "user1";
    private static final String PASSWORD = "passwd1";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final MockHttpServletRequestBuilder[] UNAUTHENTICATED_REQUESTS = {
            post("/category").contentType("application/json"),
            delete("/category").contentType("application/json"),
            get("/category").contentType("application/json"),
            put("/category/123").contentType("application/json")
    };

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource
    @SneakyThrows
    void testUnauthenticatedRequests(MockHttpServletRequestBuilder request){
        this.mockMvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    static Stream<Arguments> testUnauthenticatedRequests() {
        return Stream.of(UNAUTHENTICATED_REQUESTS).map(Arguments::of);
    }

    @SneakyThrows
    @Test
    void getAllCategory() {
        this.mockMvc.perform(get("/category")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void addCategory() {
        this.mockMvc.perform(post("/category")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getAddCategoryRequest()))
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @SneakyThrows
    @Test
    void updateCategory() {
        this.mockMvc.perform(put("/category/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getAddCategoryRequest()))
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().is(204))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void updateCategoryIDDoesNotExist() {
        this.mockMvc.perform(put("/category/12345678")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getAddCategoryRequest()))
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Category with id 12345678 not found")))
                .andExpect(jsonPath("$.code", is("RESOURCE_NOT_FOUND")));
    }

    private static AddCategoryRequest getAddCategoryRequest() {
        AddCategoryRequest addCategoryRequest = new AddCategoryRequest();
        addCategoryRequest.setName("test");
        addCategoryRequest.setDescription("test description");
        return addCategoryRequest;
    }

    @SneakyThrows
    @Test
    void deleteCategory() {
        this.mockMvc.perform(delete("/category/1")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void deleteCategoryIDDoesNotExist() {
        this.mockMvc.perform(delete("/category/12345678")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Category with id 12345678 not found")))
                .andExpect(jsonPath("$.code", is("RESOURCE_NOT_FOUND")));
    }


}