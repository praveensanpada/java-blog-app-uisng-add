package com.blog.app.configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.blog.app.service.CustomAuthorDetailService;
import com.blog.app.util.JwtUtil;



@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	CustomAuthorDetailService customAuthorDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String authorizationHeader = request
			.getHeader("Authorization");
		String email = null;
		String jwtToken = null;

		// Validating the header token
		if (authorizationHeader != null && authorizationHeader
			.startsWith("Bearer ")) {
			jwtToken = authorizationHeader
				.substring(7);

			email = this.jwtUtil
				.extractUsername(jwtToken);
			try {
			} catch (Exception e) {
				e
					.printStackTrace();
			}

			UserDetails userDetails = this.customAuthorDetailService
				.loadUserByUsername(email);
			if (email != null && SecurityContextHolder
				.getContext()
				.getAuthentication() == null) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails
							.getAuthorities());
				usernamePasswordAuthenticationToken
					.setDetails(new WebAuthenticationDetailsSource()
						.buildDetails(request));
				SecurityContextHolder
					.getContext()
					.setAuthentication(usernamePasswordAuthenticationToken);
			} else {
				System.out
					.println("Token is not valid.");
			}

		} else {
			System.out
				.println("Invalid auth header format.");
		}

		filterChain
			.doFilter(request, response);

	}

}
