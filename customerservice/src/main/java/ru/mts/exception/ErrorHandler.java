package ru.mts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

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

    @ExceptionHandler({MissingPathVariableException.class})
    public ResponseEntity<String> handleMissingPathVariableException(final Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Проверьте правильность ссылки на требуемый ресурс ");
    }

    @ExceptionHandler({NoHandlerFoundException.class, ResourceNotFoundException.class})
    public ResponseEntity<String> handleNoHandlerFoundException(final Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Ресурс не найден: " + e.getMessage());
    }

//    @ExceptionHandler(Exception.class)
//    public ModelAndView handleError404(HttpServletRequest request, Exception e)   {
//        ModelAndView mav = new ModelAndView("/404");
//        mav.addObject("exception", e);
//        mav.addObject("errorcode", "404");
//        return mav;
//    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleThrowable(final Throwable e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unhandled exception." + e.getMessage());
    }
}
