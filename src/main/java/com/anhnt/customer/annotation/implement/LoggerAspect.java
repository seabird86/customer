package com.anhnt.customer.annotation.implement;

import com.anhnt.customer.annotation.LogAround;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Aspect
@Configuration
@Slf4j
public class LoggerAspect {

    @Before("within(com.anhnt..*) && @annotation(com.anhnt.customer.annotation.LogAround)")
    public void before(JoinPoint joinPoint) throws Throwable {
        log.info("===== Begin: " + this.getMessage(joinPoint) + " =====");
    }

    @AfterReturning("within(com.anhnt..*) && @annotation(com.anhnt.customer.annotation.LogAround)")
    public void afterReturning(JoinPoint joinPoint) throws Throwable {
        log.info("===== End: [Return] " + this.getMessage(joinPoint) + " =====");
    }

//    @After(value = "within(com.anhnt..*) && @annotation(com.anhnt.customer.annotation.LogAround)")
//    public void after(JoinPoint joinPoint) throws Throwable {
//        log.info("===== End: =====");
//    }

    @AfterThrowing(value = "within(com.anhnt..*) && @annotation(com.anhnt.customer.annotation.LogAround)", throwing="e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
//        log.error("There is an error occur in process", e);
        log.info("===== End [Exception] " + this.getMessage(joinPoint) + " =====");
    }

    private String getMessage(JoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//        Method implementMethod = joinPoint.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
        if (method.isAnnotationPresent(LogAround.class)) {
            LogAround logAround = method.getAnnotation(LogAround.class);
            return logAround.message();
        }
        return null;
    }

}
