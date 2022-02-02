package com.blog.app.entity;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomAuthorDetail implements UserDetails {

	private Author author;

	public CustomAuthorDetail(Author author) {
		this.author = author;
	}

	public Author getAuthor() {
		return this.author;
	}

	public int getAuthorId() {
		return this.author
			.getId();
	}

	public String getAuthorName() {
		return this.author
			.getName();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		HashSet<SimpleGrantedAuthority> set = new HashSet<>();
		set
			.add(new SimpleGrantedAuthority(this.author
				.getRole()));
		return set;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.author
			.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.author
			.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
