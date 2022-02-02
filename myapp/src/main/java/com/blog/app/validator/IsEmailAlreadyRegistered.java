package com.blog.app.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = IsEmailAlreadyRegisteredValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsEmailAlreadyRegistered {
	String message() default "Email is already registered with us";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
