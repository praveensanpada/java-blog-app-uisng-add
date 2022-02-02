package com.blog.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.dto.AuthorJwtDTO;
import com.blog.app.dto.JwtTokenDTO;
import com.blog.app.entity.Author;
import com.blog.app.entity.Blog;
import com.blog.app.service.AuthorService;
import com.blog.app.service.BlogService;
import com.blog.app.service.CustomAuthorDetailService;
import com.blog.app.util.JwtUtil;

@RestController
@RequestMapping("/api")
public class JwtController {

	@Autowired
	CustomAuthorDetailService customAuthorDetailService;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	AuthorService authorService;

	@Autowired
	BlogService blogService;

	@RequestMapping(value = "/login")
	public ResponseEntity<?> authorLogin(@RequestBody AuthorJwtDTO authorJwtDTO) throws Exception {

		System.out
			.println(authorJwtDTO);
		try {
			this.authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authorJwtDTO
					.getEmail(),
						authorJwtDTO
							.getPassword()));
			System.out
				.println("Authentication success");
		} catch (UsernameNotFoundException e) {
			e
				.printStackTrace();
			throw new Exception("Email not found");

		} catch (BadCredentialsException e) {
			e
				.printStackTrace();
			throw new Exception("Bad Credentials");
		}

		// IF USER IS AUTHENTICATED
		UserDetails userDetails = this.customAuthorDetailService
			.loadUserByUsername(authorJwtDTO
				.getEmail());

		String token = this.jwtUtil
			.generateToken(userDetails);

		return ResponseEntity
			.ok(new JwtTokenDTO(token));
	}

	@PostMapping(value = "/register")
	public ResponseEntity<?> authorRegisterPost(@Valid @RequestBody Author author, BindingResult result) {
		try {

			if (result
				.hasErrors()) {
				List<FieldError> fieldErrors = result
					.getFieldErrors();
				return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
			}

			Author a = authorService
				.addAuthor(author);
			if (a == null) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			} else {
				return ResponseEntity
					.ok(a);
			}

		} catch (Exception e) {
			e
				.printStackTrace();
			return null;
		}
	}

	@PostMapping(value = "/add-blog")
	public ResponseEntity<?> addBlog(@Valid @RequestBody Blog blog, BindingResult result,
			Authentication authentication) {
		Author author = this.authorService
			.getAuthorByEmail(authentication
				.getName());

		Blog savedBlog = blogService
			.addAuthorBlog(author, blog, result);
		if (savedBlog == null) {
			if (result
				.hasErrors()) {
				List<FieldError> fieldErrors = result
					.getFieldErrors();
				return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(savedBlog, HttpStatus.ACCEPTED);
		}

	}

	// Using second type of authmethod
	@PostMapping(value = "/api/edit-blog")
	public ResponseEntity<?> editBlog(@Valid @RequestBody Blog blog, BindingResult result,
			Authentication authentication) {
		Author author = this.authorService
			.getAuthorByEmail(authentication
				.getName());

		Blog savedBlog = blogService
			.editAuthorBlog(author, blog, result);
		if (savedBlog == null) {
			if (result
				.hasErrors()) {
				List<FieldError> fieldErrors = result
					.getFieldErrors();
				return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} else {

			return new ResponseEntity<>(savedBlog, HttpStatus.ACCEPTED);
		}

	}

	@GetMapping(value = "/blogs")
	public ResponseEntity<?> getAuthorBlogs(Authentication authentication) {
		Author author = this.authorService
			.getAuthorByEmail(authentication
				.getName());

		return new ResponseEntity<>(author
			.getBlogs(), HttpStatus.ACCEPTED);
	}

}
