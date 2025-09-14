package ru.poteha.rent.context.web.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.annotation.AliasFor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping
@PreAuthorize("({authorize})")
@Operation(security = @SecurityRequirement(name = "auth"))
public @interface ApiOperation {
    @AliasFor(attribute = "name", annotation = RequestMapping.class)
    String name() default "";

    @AliasFor(attribute = "path", annotation = RequestMapping.class)
    String[] value() default {};

    @AliasFor(attribute = "value", annotation = RequestMapping.class)
    String[] path() default {};

    @AliasFor(attribute = "method", annotation = RequestMapping.class)
    RequestMethod[] method() default {};

    @AliasFor(attribute = "params", annotation = RequestMapping.class)
    String[] params() default {};

    @AliasFor(attribute = "headers", annotation = RequestMapping.class)
    String[] headers() default {};

    @AliasFor(attribute = "consumes", annotation = RequestMapping.class)
    String[] consumes() default {};

    @AliasFor(attribute = "produces", annotation = RequestMapping.class)
    String[] produces() default {};

    @AliasFor(attribute = "summary", annotation = Operation.class)
    String description() default "";

    @AliasFor(attribute = "tags", annotation = Operation.class)
    String[] tags() default {};

    String authorize() default "isAuthenticated()";
}
