package ch.cern.todo.error;

import ch.cern.todo.openapi.model.Error;
import org.springframework.http.HttpStatusCode;

public class IncorrectUsernameOrPasswordException extends TodoApplicationException {

    public IncorrectUsernameOrPasswordException() {
        super("Incorrect username or password", Error.CodeEnum.INVALID_USER_OR_PASSWORD, HttpStatusCode.valueOf(401));
    }

}
