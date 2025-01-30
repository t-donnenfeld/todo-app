package ch.cern.todo.controller;


import ch.cern.todo.openapi.api.TodoApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TodoController implements TodoApi {
}
