package com.blog.app.dto;

import java.util.List;

import com.blog.app.entity.Blog;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public class AuthorDTO {
	private String email;

	private String name;

	@JsonManagedReference
	private List<Blog> blogs;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Blog> getBlogs() {
		return blogs;
	}

	public void setBlogs(List<Blog> blogs) {
		this.blogs = blogs;
	}
}
