package com.blog.app.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.blog.app.dto.AuthorDTO;
import com.blog.app.entity.Author;
import com.blog.app.entity.Blog;
import com.blog.app.repository.AuthorRepository;


@Service
public class AuthorService {

	@Autowired
	AuthorRepository authorRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	public AuthorService() {

	}

	public Author getAuthorByEmail(String email) {
		return this.authorRepository
			.getAuthorByEmail(email);
	}

	public List<Blog> getAuthorBlogs(String email) {
		try {
			Author author = authorRepository
				.getAuthorByEmail(email);
			return author
				.getBlogs();
		} catch (Exception e) {
			System.out
				.println(e);
			return null;
		}
	}

	public Author addAuthor(Author author) {
		try {
			author
				.setPassword(this.bCryptPasswordEncoder
					.encode(author
						.getPassword()));
			author
				.setRole("ROLE_AUTHOR");
			Author newAuthor = authorRepository
				.save(author);
			return newAuthor;

		} catch (Exception e) {
			// TODO: handle exception
			System.out
				.println(e);
			e
				.getStackTrace();
			return null;
		}
	}

	public Author addAuthor1(Author author) {
		try {
			Author a = authorRepository
				.getAuthorByEmail(author
					.getEmail());
			if (a == null) {
				return (Author) authorRepository
					.save(author);
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e
				.getStackTrace();
			return null;
		}
	}

	public List<Author> getAuthor() {
		try {
			return (List<Author>) authorRepository
				.findAll();
		} catch (Exception e) {
			e
				.printStackTrace();
			return null;
		}
	}

	public String deleteAuthor(@PathVariable int id) {
		try {
			Optional<Author> author = authorRepository
				.findById(id);
			System.out
				.println(author);
			authorRepository
				.delete(author
					.get());
			return "Deleted";
		} catch (Exception e) {
			e
				.printStackTrace();
			return null;
		}
	}

	public AuthorDTO getAllBlogsOfAuthor(@PathVariable int id) {
		try {
			Optional<Author> authorOptional = authorRepository
				.findById(id);
			Author author = authorOptional
				.get();

			AuthorDTO dto = modelMapper
				.map(author, AuthorDTO.class);

			return dto;
		} catch (Exception e) {
			e
				.printStackTrace();
			return null;
		}
	}
}
