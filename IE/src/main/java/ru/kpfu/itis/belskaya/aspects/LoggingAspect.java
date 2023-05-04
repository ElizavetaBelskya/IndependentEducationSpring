package ru.kpfu.itis.belskaya.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger logger = Logger.getLogger(LoggingAspect.class.getName());
    private final String LOG_FORMAT = "Exception: %s with cause: %s and message: %s";

    @Before("@annotation(org.springframework.web.bind.annotation.ExceptionHandler) && args(java.lang.Throwable, ..)")
    public void logMethod(JoinPoint joinPoint) {

        Throwable throwable = (Throwable) joinPoint.getArgs()[0];
        ResponseStatus annotation = AnnotationUtils.findAnnotation(throwable.getClass(), ResponseStatus.class);

        Class<?> clazz = throwable.getClass();

        if (clazz.equals(AccessDeniedException.class) || clazz.equals(MethodArgumentNotValidException.class) ||
                annotation != null &&  annotation.value().value() < HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            logger.log(Level.INFO, String.format(LOG_FORMAT, clazz, throwable.getCause(), throwable.getMessage()));
        } else {
            logger.log(Level.WARNING, String.format(LOG_FORMAT, clazz, throwable.getCause(), throwable.getMessage()));
        }
    }


}
