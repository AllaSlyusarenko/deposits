package ru.mts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<String> handleDataException(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Неверные входные данные: " + e.getMessage());
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Ресурс не найден: " + e.getMessage());
    }
    @ExceptionHandler({UnexpectedException.class})
    public ResponseEntity<String> handleUnexpectedException(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Неверные данные: " + e.getMessage());
    }

    @ExceptionHandler({MissingPathVariableException.class})
    public ResponseEntity<String> handleMissingPathVariableException(final Throwable e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Проверьте правильность ссылки на требуемый ресурс ");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleThrowable(final Throwable e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unhandled exception." + e.getMessage());
    }
}
