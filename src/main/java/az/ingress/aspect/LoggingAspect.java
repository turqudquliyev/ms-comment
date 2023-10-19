package az.ingress.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Around("execution(* az.ingress.service.concrete.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        var methodName = joinPoint.getSignature().getName();
        var params = joinPoint.getArgs();
        log.info("ActionLog.{}.start", methodName);
        log.info("ActionLog.{} params: {}", methodName, params);
        var returned = joinPoint.proceed();
        if (returned != null) log.info("ActionLog.{} returned {}", methodName, returned);
        log.info("ActionLog.{}.end", methodName);
        return returned;
    }
}