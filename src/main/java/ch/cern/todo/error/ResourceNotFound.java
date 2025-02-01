package ch.cern.todo.error;

import ch.cern.todo.openapi.model.Error;
import org.springframework.http.HttpStatusCode;

public class ResourceNotFound extends TodoApplicationException {
    public ResourceNotFound(String message) {
        super(message, Error.CodeEnum.RESOURCE_NOT_FOUND, HttpStatusCode.valueOf(404));
    }
}
