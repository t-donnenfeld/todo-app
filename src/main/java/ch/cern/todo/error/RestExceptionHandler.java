package ch.cern.todo.error;

import ch.cern.todo.openapi.model.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> defaultHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
        DefaultInternalServerException error = new DefaultInternalServerException(ex.getMessage(), ex);
        return new ResponseEntity<>(error.buildError(), HttpStatusCode.valueOf(500));
    }

    @ExceptionHandler(TodoApplicationException.class)
    public ResponseEntity<Error> applicationExceptionHandler(TodoApplicationException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(ex.buildError(), ex.getHttpStatusCode());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Error> applicationExceptionHandler(AuthorizationDeniedException ex) {
        log.error(ex.getMessage(), ex);
        Error error = new Error();
        error.setCode(Error.CodeEnum.AUTHORIZATION_DENIED);
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatusCode.valueOf(403));
    }


}
