package ch.cern.todo.error;

import ch.cern.todo.openapi.model.Error;
import org.springframework.http.HttpStatusCode;

public class NotImplementedException extends TodoApplicationException {

    public NotImplementedException(String message) {
        super(message, Error.CodeEnum.DEFAULT_ERROR, HttpStatusCode.valueOf(501));
    }

}
