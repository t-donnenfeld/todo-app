package ch.cern.todo.error;

public class DefaultInternalServerError extends TodoApplicationException {

    public DefaultInternalServerError(String message, Throwable cause) {
        super(message, cause);
    }

}
