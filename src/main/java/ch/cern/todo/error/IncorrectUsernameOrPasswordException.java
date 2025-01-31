package ch.cern.todo.error;

import org.springframework.http.HttpStatusCode;

public class IncorrectUsernameOrPasswordException extends TodoApplicationException {

    public IncorrectUsernameOrPasswordException() {
        super("Incorrect username or password", HttpStatusCode.valueOf(401));
    }

}
