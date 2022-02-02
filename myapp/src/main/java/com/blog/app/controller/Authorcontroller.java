package com.blog.app.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blog.app.dto.AuthorDTO;
import com.blog.app.entity.Blog;
import com.blog.app.service.AuthorService;

@Controller
@RequestMapping(value = "/author")
public class Authorcontroller {
	@Autowired
	AuthorService authorService;

	@GetMapping(value = "/dashboard")
	public String authorDashboard(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "error", defaultValue = "0") int error,
			@RequestParam(value = "success", defaultValue = "0") int success, Authentication authentication,
			Model model, HttpSession httpSession) {

		if (error != 1) {
			httpSession
				.removeAttribute("error");
		}
		if (success != 1) {
			httpSession
				.removeAttribute("message");
		}

		System.out
			.println(model
				.getAttribute("error"));
		System.out
			.println(model
				.getAttribute("message"));

		String email = authentication
			.getName();
		List<Blog> blogs = authorService
			.getAuthorBlogs(email);

		model
			.addAttribute("blogs", blogs);

		return "/author/dashboard";
	}

	@GetMapping(value = { "/author/{id}" })
	public AuthorDTO getAllBlogsOfAuthor(@PathVariable int id) {
		return authorService
			.getAllBlogsOfAuthor(id);
	}

}
