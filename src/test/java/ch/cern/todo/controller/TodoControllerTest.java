package ch.cern.todo.controller;

import ch.cern.todo.openapi.model.AddTodoRequest;
import ch.cern.todo.openapi.model.AddTodoRequestCategory;
import ch.cern.todo.openapi.model.SearchMyTodosRequest;
import ch.cern.todo.openapi.model.SearchTodosRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/setup_test_database.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/add_users.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/add_category.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/scripts/add_todos.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/scripts/tear_down_database.sql")
@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    private static final String USERNAME = "user1";
    private static final String PASSWORD = "passwd1";

    private static final String ADMIN_USERNAME = "theotime";
    private static final String ADMIN_PASSWORD = "donnenfeld";

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

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
    void testUnauthenticatedRequests(MockHttpServletRequestBuilder request) {
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
                        .with(httpBasic(USERNAME, PASSWORD))
                        .content(objectMapper.writeValueAsString(getAddTodoRequest())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private static AddTodoRequest getAddTodoRequest() {
        AddTodoRequest addTodoRequest = new AddTodoRequest();
        AddTodoRequestCategory category = new AddTodoRequestCategory();
        category.setName("category");
        addTodoRequest.setCategory(category);
        addTodoRequest.setDescription("description");
        addTodoRequest.setName("description");
        addTodoRequest.setDeadline(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 0, 0)));
        return addTodoRequest;
    }

    @SneakyThrows
    @Test
    void deleteTodoAsOwner() {
        this.mockMvc.perform(delete("/todo/2")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void deleteOtherUserTodo() {
        this.mockMvc.perform(delete("/todo/1")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isForbidden());
    }

    @SneakyThrows
    @Test
    void deleteOtherUserTodoAsAdmin() {
        this.mockMvc.perform(delete("/todo/1")
                        .contentType("application/json")
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void deleteTodoIDDoesNotExist() {
        this.mockMvc.perform(delete("/todo/12345678")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Could not find todo with id 12345678")))
                .andExpect(jsonPath("$.code", is("RESOURCE_NOT_FOUND")));
    }

    @SneakyThrows
    @Test
    void getAllTodoAsAdmin() {
        this.mockMvc.perform(get("/todo")
                        .contentType("application/json")
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void getAllTodoAsUser() {
        this.mockMvc.perform(get("/todo")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isForbidden());
    }

    @SneakyThrows
    @Test
    void getOtherUserTodoById() {
        this.mockMvc.perform(get("/todo/1")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isForbidden());
    }

    @SneakyThrows
    @Test
    void getOtherUserTodoByIdAsAdmin() {
        this.mockMvc.perform(get("/todo/2")
                        .contentType("application/json")
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("myUserTodo")))
                .andExpect(jsonPath("$.description", is("mytodo is ...")));
    }

    @SneakyThrows
    @Test
    void getTodoByIdAsOwner() {
        this.mockMvc.perform(get("/todo/2")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("myUserTodo")))
                .andExpect(jsonPath("$.description", is("mytodo is ...")));
    }

    @SneakyThrows
    @Test
    void updateTodo() {
        this.mockMvc.perform(put("/todo/2")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD))
                        .content(objectMapper.writeValueAsString(getAddTodoRequest())))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void updateOtherUserTodo() {
        this.mockMvc.perform(put("/todo/1")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD))
                        .content(objectMapper.writeValueAsString(getAddTodoRequest())))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void updateOtherUserTodoAsAdmin() {
        this.mockMvc.perform(put("/todo/1")
                        .contentType("application/json")
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD))
                        .content(objectMapper.writeValueAsString(getAddTodoRequest())))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void getMyTodos() {
        this.mockMvc.perform(get("/todo/mine")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource
    void searchMyTodos(SearchMyTodosRequest inputRequest, int expectedFoundNumber) {
        this.mockMvc.perform(post("/todo/mine/search")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD))
                        .content(objectMapper.writeValueAsString(inputRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(expectedFoundNumber)));
    }

    private static Stream<Arguments> searchMyTodos() {
        SearchMyTodosRequest searchDescription1 = new SearchMyTodosRequest();
        searchDescription1.setDescriptionContains("mytodo3");

        SearchMyTodosRequest searchDescription2 = new SearchMyTodosRequest();
        searchDescription2.setDescriptionContains("azeazdza");

        SearchMyTodosRequest searchName1 = new SearchMyTodosRequest();
        searchName1.setNameContains("myUserTodo");

        SearchMyTodosRequest searchName2 = new SearchMyTodosRequest();
        searchName2.setNameContains("myAdminTodo");

        SearchMyTodosRequest searchCategory1 = new SearchMyTodosRequest();
        searchCategory1.categoryIs("category");

        SearchMyTodosRequest searchCategory2 = new SearchMyTodosRequest();
        searchCategory2.categoryIs("category2");

        SearchMyTodosRequest searchCategory3 = new SearchMyTodosRequest();
        searchCategory3.categoryIs("unkown category");

        SearchMyTodosRequest searchDeadlineAfter1 = new SearchMyTodosRequest();
        searchDeadlineAfter1.setDeadlineAfter(LocalDateTime.of(LocalDate.of(2025, 1, 1), LocalTime.of(1, 0, 0)));

        SearchMyTodosRequest searchDeadlineAfter2 = new SearchMyTodosRequest();
        searchDeadlineAfter2.setDeadlineAfter(LocalDateTime.of(LocalDate.of(2022, 1, 1), LocalTime.of(1, 0, 0)));

        SearchMyTodosRequest searchDeadlineBefore1 = new SearchMyTodosRequest();
        searchDeadlineBefore1.setDeadlineBefore(LocalDateTime.of(LocalDate.of(2025, 1, 1), LocalTime.of(1, 0, 0)));

        SearchMyTodosRequest searchDeadlineBefore2 = new SearchMyTodosRequest();
        searchDeadlineBefore2.setDeadlineBefore(LocalDateTime.of(LocalDate.of(2026, 1, 1), LocalTime.of(1, 0, 0)));

        SearchMyTodosRequest impossibleDateSearch = new SearchMyTodosRequest();
        impossibleDateSearch.setDeadlineAfter(LocalDateTime.of(LocalDate.of(2025, 1, 1), LocalTime.of(1, 0, 0)));
        impossibleDateSearch.setDeadlineBefore(LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.of(1, 0, 0)));

        SearchMyTodosRequest combinedSearch = new SearchMyTodosRequest();
        combinedSearch.setDeadlineBefore(LocalDateTime.of(LocalDate.of(2025, 1, 1), LocalTime.of(1, 0, 0)));
        combinedSearch.setNameContains("myUserTodo");
        combinedSearch.setDescriptionContains("mytodo");
        combinedSearch.setCategoryIs("category2");

        return Stream.of(
                Arguments.of(searchDescription1, 1),
                Arguments.of(searchDescription2, 0),
                Arguments.of(searchName1, 3),
                Arguments.of(searchName2, 0),
                Arguments.of(searchCategory1, 1),
                Arguments.of(searchCategory2, 2),
                Arguments.of(searchCategory3, 0),
                Arguments.of(searchDeadlineAfter1, 1),
                Arguments.of(searchDeadlineAfter2, 3),
                Arguments.of(searchDeadlineBefore1, 2),
                Arguments.of(searchDeadlineBefore2, 3),
                Arguments.of(impossibleDateSearch, 0),
                Arguments.of(combinedSearch, 2)
        );
    }

    @Test
    void searchTodosAsUser() throws Exception {
        this.mockMvc.perform(post("/todo/search")
                        .contentType("application/json")
                        .with(httpBasic(USERNAME, PASSWORD))
                        .content(objectMapper.writeValueAsString(new SearchTodosRequest())))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource
    void searchTodos(SearchTodosRequest inputRequest, int expectedFoundNumber) {
        this.mockMvc.perform(post("/todo/search")
                        .contentType("application/json")
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD))
                        .content(objectMapper.writeValueAsString(inputRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(expectedFoundNumber)));
    }

   private static  Stream<Arguments> searchTodos() {
       SearchTodosRequest searchDescription1 = new SearchTodosRequest();
       searchDescription1.setDescriptionContains("mytodo3");

       SearchTodosRequest searchName1 = new SearchTodosRequest();
       searchName1.setNameContains("myUserTodo");

       SearchTodosRequest searchCategory1 = new SearchTodosRequest();
       searchCategory1.categoryIs("category");

       SearchTodosRequest searchDeadlineAfter1 = new SearchTodosRequest();
       searchDeadlineAfter1.setDeadlineAfter(LocalDateTime.of(LocalDate.of(2025, 1, 1), LocalTime.of(1, 0, 0)));

       SearchTodosRequest searchDeadlineBefore1 = new SearchTodosRequest();
       searchDeadlineBefore1.setDeadlineBefore(LocalDateTime.of(LocalDate.of(2025, 1, 1), LocalTime.of(1, 0, 0)));

       SearchTodosRequest searchOwner1 = new SearchTodosRequest();
       searchOwner1.setOwner("theotime");

       SearchTodosRequest searchOwner2 = new SearchTodosRequest();
       searchOwner2.setOwner("unknown user");

       SearchTodosRequest searchOwner3 = new SearchTodosRequest();
       searchOwner3.setOwner("user1");

       SearchTodosRequest combinedSearch = new SearchTodosRequest();
       combinedSearch.setDeadlineBefore(LocalDateTime.of(LocalDate.of(2025, 1, 1), LocalTime.of(1, 0, 0)));
       combinedSearch.setNameContains("myUserTodo");
       combinedSearch.setDescriptionContains("mytodo");
       combinedSearch.setCategoryIs("category2");
       combinedSearch.setOwner("user1");

       return Stream.of(
               Arguments.of(searchDescription1, 1),
               Arguments.of(searchName1, 3),
               Arguments.of(searchCategory1, 2),
               Arguments.of(searchDeadlineAfter1, 2),
               Arguments.of(searchDeadlineBefore1, 2),
               Arguments.of(searchOwner1, 1),
               Arguments.of(searchOwner2, 0),
               Arguments.of(searchOwner3, 3),
               Arguments.of(combinedSearch, 2)
       );
   }
}