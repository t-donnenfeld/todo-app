package ch.cern.todo.error;

import ch.cern.todo.openapi.model.Error;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public abstract class TodoApplicationException extends RuntimeException {

    private final Error.CodeEnum errorcode;
    private final HttpStatusCode httpStatusCode;

    protected TodoApplicationException(String message, Throwable cause, Error.CodeEnum errorcode, HttpStatusCode httpStatusCode) {
        super(message, cause);
        this.errorcode = errorcode;
        this.httpStatusCode = httpStatusCode;
    }

    protected TodoApplicationException(String message, Error.CodeEnum errorcode, HttpStatusCode httpStatusCode) {
        super(message);
        this.errorcode = errorcode;
        this.httpStatusCode = httpStatusCode;
    }

    public Error buildError() {
        Error error = new Error();
        error.setCode(getErrorcode());
        error.setMessage(getMessage());
        return error;
    }

}
