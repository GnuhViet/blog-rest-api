package com.example.app.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ServiceAopExpression {
    @Pointcut("execution(* com.example.app.service.UserService.*(..))")
    public void forUserService() {}
}
