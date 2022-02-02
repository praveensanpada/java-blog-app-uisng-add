package com.blog.app.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsValidPasswordValidator implements ConstraintValidator<IsValidPassword, String> {

	private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
	private Pattern pattern;
	private Matcher matcher;

	public IsValidPasswordValidator() {
		this.pattern = Pattern
			.compile(PASSWORD_PATTERN);
	}

	@Override
	public void initialize(IsValidPassword isValidPassword) {
		isValidPassword
			.message();
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (password == null)
			return false;
		matcher = pattern
			.matcher(password);
		return matcher
			.matches();
	}

}
