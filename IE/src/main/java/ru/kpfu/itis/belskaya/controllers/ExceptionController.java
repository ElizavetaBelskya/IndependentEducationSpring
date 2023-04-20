package ru.kpfu.itis.belskaya.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Elizaveta Belskaya
 */
@ControllerAdvice
public class ExceptionController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        return "/views/errorPage";
    }

    @Override
    public String getErrorPath() {
        return "/views/errorPage";
    }

}
