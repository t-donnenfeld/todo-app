package ch.cern.todo.controller;


import ch.cern.todo.openapi.api.TodoApi;
import ch.cern.todo.openapi.model.*;
import ch.cern.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
public class TodoController implements TodoApi {

    private final TodoService todoService;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Todo>> getAllTodo() {
        List<Todo> todos = todoService.findAllTodos();
        return ResponseEntity.ok(todos);
    }

    @Override
    public ResponseEntity<Todo> getTodoById(Long todoId) {
        Todo todo = todoService.findTodoById(todoId);
        return ResponseEntity.ok(todo);
    }

    @Override
    public ResponseEntity<Todo> addTodo(AddTodoRequest addTodoRequest) {
        Todo todo = todoService.addTodo(addTodoRequest);
        return new ResponseEntity<>(todo, HttpStatus.CREATED);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Todo>> searchTodos(SearchTodosRequest searchTodosRequest) {
        List<Todo> todos = todoService.searchTodos(searchTodosRequest);
        return ResponseEntity.ok(todos);
    }

    @Override
    public ResponseEntity<List<Todo>> getMyTodos() {
        List<Todo> todos = todoService.findAllTodosOfAuthenticatedUser();
        return ResponseEntity.ok(todos);
    }

    @Override
    public ResponseEntity<List<Todo>> searchMyTodos(SearchMyTodosRequest searchMyTodosRequest) {
        List<Todo> todos = todoService.searchTodosOfAuthenticatedUser(searchMyTodosRequest);
        return ResponseEntity.ok(todos);
    }

    @Override
    public ResponseEntity<Todo> updateTodo(Long todoId, AddTodoRequest addTodoRequest) {
        Todo todo = todoService.updateTodo(todoId, addTodoRequest);
        return new ResponseEntity<>(todo, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<DeletedEntryResponse> deleteTodo(Long todoId) {
        todoService.deleteTodo(todoId);

        DeletedEntryResponse deletedEntryResponse = new DeletedEntryResponse();
        deletedEntryResponse.setMessage("Todo with id " + todoId + " deleted");

        return new ResponseEntity<>(deletedEntryResponse, HttpStatus.OK);
    }
}
