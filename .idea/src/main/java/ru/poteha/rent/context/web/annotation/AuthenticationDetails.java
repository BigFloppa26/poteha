package ru.poteha.rent.context.web.annotation;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.core.annotation.CurrentSecurityContext;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Hidden
@Documented
@Retention(RUNTIME)
@Target({PARAMETER, ANNOTATION_TYPE})
@CurrentSecurityContext(expression = "authentication.details")
public @interface AuthenticationDetails {
}
