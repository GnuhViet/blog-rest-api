package com.example.app.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ServiceLoggingAspect {

    @AfterReturning(
            pointcut = "com.example.app.aspect.ServiceAopExpression.forUserService()",
            returning = "res"
    )
    public void afterReturningUserService(JoinPoint joinPoint, Object res) {
        log.info("Method: " + joinPoint.getSignature().toString() + " executed, return: " + res);
    }

    @Before("com.example.app.aspect.ServiceAopExpression.forUserService()")
    public void beforeUserService(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("Method: " + joinPoint.getSignature().toString() + " called with args: " + Arrays.toString(args));
    }


}
