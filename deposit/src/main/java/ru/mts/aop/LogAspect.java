package ru.mts.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.mts.annotation.Logging;

@Aspect
@Slf4j
@Component
public class LogAspect {
    private static final String ENTER = ">> ";
    private static final String EXIT = "<< ";
    ObjectMapper objectMapper = new ObjectMapper();

    @Around("@annotation(logging) && execution(* *(..))")
    public Object execute(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        String message = logging.value().isBlank() ? methodName : logging.value();

        if (logging.entering()) {
            levelLogging(logging.level(), ENTER + message);
        }

        if (logging.logArgs()) {
            levelLogging(logging.level(), "Arguments in method: " + methodName + " args: " + objectMapper.writeValueAsString(args));
        }
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Error on method:{}", methodName, e);
        }
        if (logging.logResult()) {
            levelLogging(logging.level(), "Result in method: " + methodName + " result: " + objectMapper.writeValueAsString(result));
        }
        if (logging.exiting()) {
            levelLogging(logging.level(), EXIT + message);
        }
        return result;
    }

    private void levelLogging(String level, String message) {
        switch (level.toUpperCase()) {
            case "TRACE":
                log.trace(message);
                break;
            case "DEBUG":
                log.debug(message);
                break;
            case "INFO":
                log.info(message);
                break;
            case "WARN":
                log.warn(message);
                break;
            case "ERROR":
                log.error(message);
                break;
            default:
                log.info(message);
        }
    }
}