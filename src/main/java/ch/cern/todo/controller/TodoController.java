package ch.cern.todo.controller;


import ch.cern.todo.error.NotImplementedException;
import ch.cern.todo.openapi.api.TodoApi;
import ch.cern.todo.openapi.model.AddTodoRequest;
import ch.cern.todo.openapi.model.SearchTodosRequest;
import ch.cern.todo.openapi.model.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoController implements TodoApi {
    @Override
    public ResponseEntity<Todo> addTodo(AddTodoRequest addTodoRequest) {
        throw new NotImplementedException("addTodo is not yet implemented");
    }

    @Override
    public ResponseEntity<Void> deleteTodo(Long todoId) {
        throw new NotImplementedException("deleteTodo is not yet implemented");
    }

    @Override
    public ResponseEntity<List<Todo>> getMyTodos() {
        return TodoApi.super.getMyTodos();
    }

    @Override
    public ResponseEntity<List<Todo>> searchMyTodos() {
        return TodoApi.super.searchMyTodos();
    }

    @Override
    public ResponseEntity<List<Todo>> searchTodos(SearchTodosRequest searchTodosRequest) {
        return TodoApi.super.searchTodos(searchTodosRequest);
    }

    @Override
    public ResponseEntity<List<Todo>> getAllTodo() {
        throw new NotImplementedException("getAllTodo is not yet implemented");
    }

    @Override
    public ResponseEntity<Todo> getTodoById(Long todoId) {
        throw new NotImplementedException("getTodoById is not yet implemented");
    }

    @Override
    public ResponseEntity<Todo> updateTodo(Long todoId, Todo todo) {
        throw new NotImplementedException("updateTodo is not yet implemented");
    }
}
