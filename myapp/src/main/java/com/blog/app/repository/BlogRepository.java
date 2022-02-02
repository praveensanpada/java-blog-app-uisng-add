package com.blog.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.app.entity.Blog;


@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
	public Page<Blog> findAll(Pageable pageable);

	public Blog findByIdAndAuthorId(int id, int authorId);

	public Blog findByTitle(String title);

	public Blog findByTitleAndIdNot(String title, int id);

}
