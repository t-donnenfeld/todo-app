package ch.cern.todo.error;

import ch.cern.todo.openapi.model.Error;
import org.springframework.http.HttpStatusCode;

public class DefaultInternalServerException extends TodoApplicationException {

    public DefaultInternalServerException(String message, Throwable cause) {
        super(message, cause, Error.CodeEnum.DEFAULT_ERROR, HttpStatusCode.valueOf(500));
    }

}
