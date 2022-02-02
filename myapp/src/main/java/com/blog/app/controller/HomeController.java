package com.blog.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.entity.Blog;
import com.blog.app.repository.BlogRepository;
import com.blog.app.service.BlogService;

@RestController
public class HomeController {

	@Autowired
	private BlogService blogService;

	@Autowired
	BlogRepository blogRepository;

	@PostMapping(value = { "/blog" })
	public Blog addBlog(@RequestBody Blog blog) {
		return this.blogService
			.addBlog(blog);
	}

	@GetMapping(value = { "/blog" })
	public List<Blog> getAuthor() {
		try {
			List<Blog> blog = blogService
				.getAllBlogs();
			return blog;
		} catch (Exception e) {
			e
				.printStackTrace();
			return null;
		}
	}
}
