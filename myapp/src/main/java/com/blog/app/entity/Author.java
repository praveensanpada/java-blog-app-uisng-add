package com.blog.app.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.blog.app.validator.IsEmailAlreadyRegistered;
import com.blog.app.validator.IsValidPassword;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor

public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(unique = true, nullable = false)
	@NotBlank(message = "Email is required")
	@Email(message = "Please enter a valid email")
	@IsEmailAlreadyRegistered
	private String email;

	@Column(nullable = false, columnDefinition = "varchar(255) default '$2a$10$Av.lsQTm9pjK9YpNi7y.k.Lgo3kE2yEzRZXq7k9.JjuImo9rSeDq.'")
	@IsValidPassword
	private String password;

	@Column(length = 50)
	@Size(min = 2, max = 50, message = "Name must be between 2 to 50 characters")
	private String name;

	@Column(columnDefinition = "VARCHAR(30) default 'ROLE_AUTHOR'")
	private String role;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<Blog> blogs;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Blog> getBlogs() {
		return blogs;
	}

	public void setBlogs(List<Blog> blogs) {
		this.blogs = blogs;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return "Author [id=" + id + ", email=" + email + ", password=" + password + ", name=" + name + ", role=" + role
				+ ", blogs=" + blogs + "]";
	}

}
