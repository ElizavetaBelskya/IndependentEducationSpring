package ru.kpfu.itis.belskaya.aspects;

import lombok.extern.log4j.Log4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


@Component
@Aspect
//@Log4j
public class LoggingAspect {

    @Pointcut("within(ru.kpfu.itis.belskaya.controllers.ExceptionController)")
    public void methods() {}

//    @Before("methods()")
//    public void logError(JoinPoint joinPoint) {
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        Annotation[] annotations = method.getAnnotations();
//        ResponseStatus status = null;
//        ExceptionHandler handler = null;
//        for (Annotation annotation : annotations) {
//            if (annotation instanceof ResponseStatus) {
//                status = (ResponseStatus) annotation;
//            }
//            if (annotation instanceof ExceptionHandler) {
//                handler = (ExceptionHandler) annotation;
//            }
//        }
//        HttpStatus code = status != null ? status.value() : null;
//        Class<? extends Throwable>[] throwableList = handler.value();
//        StringBuilder errorMessage = new StringBuilder();
//        for (Class<? extends Throwable> throwable: throwableList) {
//            errorMessage.append(throwable.getName()).append(" ");
//        }
//        if (code == HttpStatus.INTERNAL_SERVER_ERROR) {
//            log.error("Internal server error: " + errorMessage);
//        } else {
//            log.info(errorMessage);
//        }
//    }

}
