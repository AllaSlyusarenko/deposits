package ru.mts.annotation;

import ru.mts.aop.LoggingLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static ru.mts.aop.LoggingLevel.INFO;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {

    String value() default "";

    boolean entering() default false;

    boolean exiting() default false;

    LoggingLevel level() default INFO;

    boolean logArgs() default false;

    boolean logResult() default false;
}