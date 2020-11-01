package com.rmaj91.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

//    @Pointcut("within(@com.rmaj91.logging.Monitoring)")
    @Pointcut("within(@com.rmaj91.logging.Monitoring *)")
    public void beanAnnotatedWithMonitoring() {}

    @Pointcut("execution(* *(..))")
    public void allMethods() {}

    @Pointcut("allMethods() && beanAnnotatedWithMonitoring()")
    public void AllMethodsInBeanAnnotatedWithMonitoring() {}

    @Before("AllMethodsInBeanAnnotatedWithMonitoring()")
    public void before(JoinPoint joinPoint) {
        log.info("Method started: " + joinPoint.getSignature().getDeclaringType().getPackageName() + "."
                + joinPoint.getSignature().toShortString());
    }

    @AfterReturning("AllMethodsInBeanAnnotatedWithMonitoring()")
    public void afterReturning(JoinPoint joinPoint) {
        log.info("Method terminating successful: " + joinPoint.getSignature().getDeclaringType().getPackageName() + "."
                + joinPoint.getSignature().toShortString());
    }

    @AfterThrowing("AllMethodsInBeanAnnotatedWithMonitoring()")
    public void afterThrowing() {
        System.out.println("blad");
//        log.info("Exception while : " + "class: " + joinPoint.getSignature().getDeclaringType().getSimpleName()
//                    + ", method: " + joinPoint.getSignature().getName() + ". Error Message: " + e.getMessage());
    }
}
