package com.example.vet_pet.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.validation.FieldError;

import java.util.Map;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest wedRequest, ErrorAttributeOptions options) {
                return super.getErrorAttributes(wedRequest, ErrorAttributeOptions.defaults()
                        .including(ErrorAttributeOptions.Include.MESSAGE));
            }
        };
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorMessage> handleMissingParams(MissingServletRequestParameterException ex){
        String parameter =ex.getParameterName();
        log.error("{} parameter is missing", parameter);

        return ResponseEntity.badRequest().body(new ErrorMessage(String.format("parameter is missing: %s", parameter)));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessage> handleMismatchType(MethodArgumentTypeMismatchException ex) {
        String parameter = ex.getParameter().getParameterName();
        log.error("wrong data type for parameter: {}", parameter);

        return ResponseEntity.badRequest().body(new ErrorMessage(String.format("wrong data type for parameter: %s", parameter)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMismatchType(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getFieldError();
        String message = fieldError != null ? fieldError.getField() + " " + fieldError.getDefaultMessage() : ex.getMessage();
        log.error(message);
        return ResponseEntity.badRequest().body(new ErrorMessage(message));
    }


}