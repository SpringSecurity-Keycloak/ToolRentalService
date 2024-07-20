package io.rentalapp.common;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Handler to capture all exceptions thrown by the application and translate into
 * HTTP responses
 */
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle all custom/business related exceptions thrown by the application
     * @param ex ValidationException is the exception thrown by failed business rules
     * @param request
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleConflict(ValidationException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return new ResponseEntity(bodyOfResponse,HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch entity class field level validation errors
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConflict2(ConstraintViolationException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        List<String> errors = new ArrayList<String>();
        ex.getConstraintViolations().stream().anyMatch(error -> errors.add(error.getMessage()));
        return new ResponseEntity(String.join(",", errors),HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch incompatible data conversion errors
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        if (ex.getRootCause() instanceof  JsonMappingException) {
            JsonMappingException error = (JsonMappingException) ex.getRootCause();
            String fieldName = error.getPath().get(0).getFieldName();
            return new ResponseEntity("Please enter a valid value for field " + fieldName, HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Catch model class field level validation errors
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        List<String> errors = new ArrayList<String>();
        ex.getBindingResult().getFieldErrors().stream().forEach(field -> errors.add(field.getDefaultMessage()));
        return new ResponseEntity(String.join(",", errors) , HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch All Runtime exceptions
     * @param ex RuntimeException
     * @param request
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return new ResponseEntity(bodyOfResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
