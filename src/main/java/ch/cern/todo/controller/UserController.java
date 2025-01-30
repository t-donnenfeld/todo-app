package ch.cern.todo.controller;

import ch.cern.todo.openapi.api.UserApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
}
