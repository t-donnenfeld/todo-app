package ch.cern.todo.error;

import ch.cern.todo.openapi.model.Error;
import org.springframework.http.HttpStatusCode;

public class ResourceAlreadyExistsException extends TodoApplicationException {

    public ResourceAlreadyExistsException(String message) {
        super(message, Error.CodeEnum.RESOURCE_ALREADY_EXISTS, HttpStatusCode.valueOf(409));
    }

}
