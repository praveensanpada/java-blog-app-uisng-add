package com.blog.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.app.entity.Author;


public interface AuthorRepository extends JpaRepository<Author, Integer> {

	public Author getAuthorByEmail(String email);
}
