package com.blog.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.blog.app.service.CustomAuthorDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAuthorDetailService customAuthorDetailService;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http
			.cors()
			.disable()
			.csrf()
			.disable()
			.authorizeRequests()
			.antMatchers("/css/**", "/js/**", "/images/**")
			.permitAll()
			.antMatchers("/", "/index")
			.permitAll()
			.antMatchers("/author/login", "/author/register", "/author/addAuthor")
			.permitAll()
			.antMatchers("/api/login", "/api/register")
			.permitAll()
			.antMatchers("/author/**")
			.hasRole("AUTHOR")
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
			.usernameParameter("email")
			.loginPage("/author/login")
			.loginProcessingUrl("/author/doLogin")
			.defaultSuccessUrl("/author/dashboard")
			.and()
			.logout()
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.deleteCookies("SESSIONID")
			.logoutRequestMatcher(new AntPathRequestMatcher("/author/logout"))
			.logoutSuccessUrl("/author/login");

		http
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub

		auth
			.userDetailsService(customAuthorDetailService)
			.passwordEncoder(this
				.passwordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
