package ru.kpfu.itis.belskaya.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.kpfu.itis.belskaya.exceptions.NotFoundException;
import ru.kpfu.itis.belskaya.models.dto.ExceptionDto;
import ru.kpfu.itis.belskaya.models.dto.ValidationErrorDto;
import ru.kpfu.itis.belskaya.models.dto.ValidationErrorsDto;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Elizaveta Belskaya
 */
@ControllerAdvice
public class ExceptionController {

    //private final Logger logger = LogManager.getLogger(ExceptionController.class.getName());

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object catchBadRequest(HttpServletRequest req, Exception exception) {
        req.setAttribute("alert", "Sorry, your request was invalid" + exception.getMessage());
        return "/views/errorPage";
    }

    @ExceptionHandler({NoHandlerFoundException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String catchNotFoundStatus(HttpServletRequest req, Exception exception) {
        //logger.info("BELSKAYA LOGS " + exception.getMessage());
        req.setAttribute("alert", "This page does not exist");
        return "/views/errorPage";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String catchInternalErrorStatus(HttpServletRequest req, Exception exception) throws Exception {
        //logger.debug(throwable.getMessage());
        if (exception.getClass().equals(AccessDeniedException.class)) {
            throw exception;
        }
        req.setAttribute("alert", "500: Internal Server Error");
        return "/views/errorPage";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionDto> handleNotFound(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ExceptionDto.builder()
                        .message(ex.getMessage())
                        .status(ex.getStatus().value())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorsDto> handleNotFound(MethodArgumentNotValidException ex) {
        List<ValidationErrorDto> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();

            String fieldName = null;
            String objectName = error.getObjectName();

            if (error instanceof FieldError) {
                fieldName = ((FieldError)error).getField();
            }
            ValidationErrorDto errorDto = ValidationErrorDto.builder()
                    .message(errorMessage)
                    .field(fieldName)
                    .object(objectName)
                    .build();

            errors.add(errorDto);
        });

        return ResponseEntity.badRequest().body(ValidationErrorsDto.builder()
                .errors(errors)
                .build());
    }

}
