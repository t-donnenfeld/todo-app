package ch.cern.todo.controller;

import ch.cern.todo.error.NotImplementedException;
import ch.cern.todo.openapi.api.UserApi;
import ch.cern.todo.openapi.model.CreateUserRequest;
import ch.cern.todo.openapi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    @Override
    public ResponseEntity<User> createUser(CreateUserRequest createUserRequest) {
        throw new NotImplementedException("createUser is not yet implemented");
    }
}
