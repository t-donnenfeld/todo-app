package ch.cern.todo.controller;

import ch.cern.todo.openapi.api.UserApi;
import ch.cern.todo.openapi.model.CreateUserRequest;
import ch.cern.todo.openapi.model.User;
import ch.cern.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<User> createUser(CreateUserRequest createUserRequest) {
        User createdUser = userService.createUser(createUserRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
