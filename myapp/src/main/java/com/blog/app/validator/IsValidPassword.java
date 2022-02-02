package com.blog.app.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = IsValidPasswordValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValidPassword {
	String message() default "Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
