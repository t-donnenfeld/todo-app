package ch.cern.todo.error;

import ch.cern.todo.openapi.model.Error;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public abstract class TodoApplicationException extends RuntimeException {

    private Error.CodeEnum errorcode = Error.CodeEnum.DEFAULT_ERROR;
    private HttpStatusCode httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;

    public TodoApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TodoApplicationException(String message) {
        super(message);
    }

    public TodoApplicationException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public Error buildError() {
        Error error = new Error();
        error.setCode(getErrorcode());
        error.setMessage(getMessage());
        return error;
    }

}
