package com.blog.app.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.blog.app.entity.Author;
import com.blog.app.entity.Blog;
import com.blog.app.entity.CustomAuthorDetail;
import com.blog.app.service.BlogService;

@Controller
public class BlogController {

	@Autowired
	private BlogService blogService;

	@GetMapping(value = { "/", "/index" })
	public String index(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
		Pageable pageable = PageRequest
			.of(page - 1, 2, Sort
				.by("date")
				.descending()
				.and(Sort
					.by("title")));
		Page<Blog> blogs = this.blogService
			.getAllBlogsWithPagination(pageable);
		model
			.addAttribute("currentPage", page);
		model
			.addAttribute("totalPage", blogs
				.getTotalPages());
		model
			.addAttribute("totalBlog", blogs
				.getTotalElements());
		model
			.addAttribute("hasNext", blogs
				.hasNext());
		model
			.addAttribute("hasPrevious", blogs
				.hasPrevious());
		model
			.addAttribute("blogs", blogs);
		return "index";
	}

	@GetMapping(value = { "/blog/{id}" })
	public String openBlog(@RequestHeader(value = HttpHeaders.REFERER, required = false) String referrer,
			@PathVariable("id") int id, @RequestParam(value = "page", defaultValue = "1") int page, Model model) {
		Blog blog = this.blogService
			.getBlogById(id);
		model
			.addAttribute("blog", blog);
		model
			.addAttribute("oldPage", page);
		if (referrer == null) {
			referrer = "/index";
		}
		model
			.addAttribute("previousUrl", referrer);
		return "single";
	}

	@GetMapping(value = "/author/add-blog")
	public String addBlogPage(Model model, HttpSession httpSession) {
		Blog blog = new Blog();
		model
			.addAttribute("blog", blog);
		return "author/add-blog";
	}

	@PostMapping(value = "/author/add-blog", consumes = "application/x-www-form-urlencoded")
	public String addBlog(@Valid Blog blog, BindingResult result,
			@AuthenticationPrincipal CustomAuthorDetail customAuthorDetail, HttpSession httpSession, Model model) {
		Author author = customAuthorDetail
			.getAuthor();

		Blog savedBlog = blogService
			.addAuthorBlog(author, blog, result);
		if (savedBlog == null) {
			return "/author/add-blog";
		} else {
			return "redirect:/blog/" + savedBlog
				.getId();
		}

	}

	@GetMapping(value = "/author/edit-blog/{blogId}")
	public String editBlogPage(@PathVariable(name = "blogId", required = false) int blogId, Model model,
			HttpSession httpSession, @AuthenticationPrincipal CustomAuthorDetail customAuthorDetail) {
		httpSession
			.removeAttribute("error");
		int authorId = customAuthorDetail
			.getAuthorId();
		Blog blog = blogService
			.checkBlogForAuthorOwnership(blogId, authorId);

		if (blog == null) {
			return "redirect:/author/dashboard";
		}
		model
			.addAttribute("blog", blog);
		return "author/edit-blog";
	}
	@PostMapping(value = "/author/edit-blog", consumes = "application/x-www-form-urlencoded")
	public String editBlog(@Valid Blog blog, BindingResult result,
			@AuthenticationPrincipal CustomAuthorDetail customAuthorDetail, Model model) {
		Author author = customAuthorDetail
			.getAuthor();

		Blog savedBlog = blogService
			.editAuthorBlog(author, blog, result);
		if (savedBlog == null) {

			return "/author/edit-blog";
		} else {

			return "redirect:/blog/" + savedBlog
				.getId();
		}

	}

	@PostMapping(value = "/author/delete-blog", consumes = "application/x-www-form-urlencoded")
	public String deleteAuthorBlog(int blogId, @AuthenticationPrincipal CustomAuthorDetail customAuthorDetail,
			HttpSession httpSession, Model model) {
		int authorId = customAuthorDetail
			.getAuthorId();

		String result = blogService
			.deleteAuthorBlog(authorId, blogId, httpSession);
		if (result == null) {
			return "redirect:/author/dashboard?error=1";
		} else {
			return "redirect:/author/dashboard?success=1";
		}

	}

	@GetMapping("/error")
	public String handleError() {
		return "error";
	}
}
