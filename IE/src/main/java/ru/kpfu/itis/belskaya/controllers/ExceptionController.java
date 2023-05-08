package ru.kpfu.itis.belskaya.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.kpfu.itis.belskaya.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Elizaveta Belskaya
 */
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({NoHandlerFoundException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String catchNotFoundStatus(HttpServletRequest req, Exception exception) {
        req.setAttribute("alert", "This page does not exist");
        return "/views/errorPage";
    }

//    @ExceptionHandler(Throwable.class)
    //TODO: убарть
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String catchInternalErrorStatus(HttpServletRequest req, Exception exception) {
        req.setAttribute("alert", "505: Internal Server Error");
        return "/views/errorPage";
    }


}
