package com.blog.app.service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.blog.app.dto.BlogDTO;
import com.blog.app.entity.Author;
import com.blog.app.entity.Blog;
import com.blog.app.repository.BlogRepository;

@Component
public class BlogService {

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private ModelMapper modelMapper;

	public Blog addBlog(Blog blog) {
		try {
			Blog result = blogRepository
				.save(blog);
			return result;
		} catch (Exception e) {
			System.out
				.println(e);
			return null;
		}
	}

	public List<Blog> getAllBlogs() {
		try {
			List<Blog> blog = (List<Blog>) blogRepository
				.findAll();

			return blog;
		} catch (Exception e) {
			System.out
				.println(e);
			return null;
		}
	}

	public List<BlogDTO> getAllBlogsDTO() {
		try {
			List<Blog> blog = (List<Blog>) blogRepository
				.findAll();

			List<BlogDTO> dto = Arrays
				.asList(modelMapper
					.map(blog, BlogDTO[].class));
			return dto;
		} catch (Exception e) {
			System.out
				.println(e);
			return null;
		}
	}

	public Blog getBlogById(int id) {
		Optional<Blog> blogOptional = null;
		Blog blog = null;
		try {
			blogOptional = this.blogRepository
				.findById(id);
			blog = blogOptional
				.get();
		} catch (Exception e) {
			e
				.printStackTrace();
		} finally {
			return blog;
		}
	}

	public Page<Blog> getAllBlogsWithPagination(Pageable pageable) {
		try {
			Page<Blog> blog = (Page<Blog>) blogRepository
				.findAll(pageable);
			return blog;
		} catch (Exception e) {
			System.out
				.println(e);
			return null;
		}
	}

	public Blog addAuthorBlog(Author author, Blog blog, BindingResult result) {
		Blog savedBlog = null;
		try {

			if (blogRepository
				.findByTitle(blog
					.getTitle()) != null) {
				result
					.rejectValue("title", "blog.title",
							"Blog with same title already exists, please use a different title.");
			}
			if (result
				.hasErrors()) {
				return null;
			}
			SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
			Date date = new Date();
			String todaysDate = formatter
				.format(date);

			blog
				.setAuthor(author);
			blog
				.setDate(todaysDate);

			savedBlog = blogRepository
				.save(blog);
			return savedBlog;
		} catch (Exception e) {
			e
				.printStackTrace();
			return null;
		}

	}

	public Blog checkBlogForAuthorOwnership(int blogId, int authorId) {
//		Optional<Blog> blog = blogRepository
//			.findById(blogId, authorId);
//		System.out
//			.println(blog);
//		if (blog
//			.isEmpty()) {
//			return null;
//		}
//		return blog
//			.get();
		Blog blog = blogRepository
			.findByIdAndAuthorId(blogId, authorId);
		return blog;
	}

	public Blog editAuthorBlog(Author author, Blog blog, BindingResult result) {
		Blog savedBlog = null;
		try {

			Blog oldBlog = this
				.checkBlogForAuthorOwnership(blog
					.getId(),
						author
							.getId());
			if (oldBlog == null) {
				result
					.rejectValue("title", "blog.title", "The blog you are trying to edit does not belongs to you.");
				;
			}

			if (blogRepository
				.findByTitleAndIdNot(blog
					.getTitle(),
						blog
							.getId()) != null) {
				result
					.rejectValue("title", "blog.title",
							"Blog with same title already exists, please use a different title.");
			}
			if (result
				.hasErrors()) {
				return null;
			}

			oldBlog
				.setTitle(blog
					.getTitle());
			oldBlog
				.setPhoto(blog
					.getPhoto());
			oldBlog
				.setShortDescription(blog
					.getShortDescription());
			oldBlog
				.setLongDescription(blog
					.getLongDescription());
			savedBlog = blogRepository
				.save(oldBlog);
			return savedBlog;
		} catch (Exception e) {
			e
				.printStackTrace();
			return savedBlog;
		}

	}

	public String deleteAuthorBlog(int authorId, int blogId, HttpSession httpSession) {
		try {
			Blog blog = this
				.checkBlogForAuthorOwnership(blogId, authorId);
			if (blog == null) {
				httpSession
					.setAttribute("error", "The blog you are trying to delete does not belongs to you.");
				return null;
			} else {
				blogRepository
					.deleteById(blogId);
				httpSession
					.setAttribute("message", "Blog deleted.");
				return "Success";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e
				.getStackTrace();
			httpSession
				.setAttribute("error", "Something went wrong while deleting blog.");
			return null;
		}
	}
}
