package com.blog.app.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.blog.app.repository.AuthorRepository;


public class IsEmailAlreadyRegisteredValidator implements ConstraintValidator<IsEmailAlreadyRegistered, String> {

	@Autowired
	AuthorRepository authorRepository;

	@Override
	public void initialize(IsEmailAlreadyRegistered isEmailAlreadyRegistered) {
		isEmailAlreadyRegistered
			.message();
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {

		if (authorRepository
			.getAuthorByEmail(email) == null)
			return true;
		else
			return false;
	}

}
