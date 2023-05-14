package ru.kpfu.itis.belskaya.aspects;

import lombok.extern.log4j.Log4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Component
@Aspect
@Log4j
public class LoggingAspect {

    static {
        log.info("BELSKAYA LOGS: added to Application successfully");
    }

//    @Before("@annotation(org.springframework.web.bind.annotation.ExceptionHandler) && args(java.lang.Throwable, ..)")
//    public void logMethod(JoinPoint joinPoint) {
//
//    }

    @Pointcut("execution(* ru.kpfu.itis.belskaya..*.*(..))")
    public void trackMethods() {}

    @Before("trackMethods()")
    public void beforeMethodCall(JoinPoint joinPoint) {
        // Получение информации о вызываемом методе
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        // Вывод информации о вызываемом методе
        System.out.println("Вызов метода: " + className + "." + methodName);
    }



}
