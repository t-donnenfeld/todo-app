package ch.cern.todo.error;

import ch.cern.todo.openapi.model.Error;
import org.springframework.http.HttpStatusCode;

public class UserAlreadyExistsException extends TodoApplicationException {

    public UserAlreadyExistsException(String message) {
        super(message, Error.CodeEnum.USER_ALREADY_EXISTS, HttpStatusCode.valueOf(409));
    }

}
