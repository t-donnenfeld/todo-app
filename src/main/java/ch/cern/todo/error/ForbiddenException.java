package ch.cern.todo.error;

import ch.cern.todo.openapi.model.Error;
import org.springframework.http.HttpStatusCode;

public class ForbiddenException extends TodoApplicationException {

    public ForbiddenException(String message) {
        super(message, Error.CodeEnum.AUTHORIZATION_DENIED, HttpStatusCode.valueOf(403));
    }

}
