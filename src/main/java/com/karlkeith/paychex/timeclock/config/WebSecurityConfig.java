package com.karlkeith.paychex.timeclock.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	
	@Bean 
	 public SecurityFilterChain filterChain(HttpSecurity http, DataSource datasource) throws Exception {
		
		// Configure AuthenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService(datasource)).passwordEncoder(bCryptPasswordEncoder);
        
	    http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/signup").permitAll()
				.requestMatchers("/signup/newUser").permitAll()
				.requestMatchers("/reports/**").hasRole("ADMIN")
				.anyRequest().authenticated());
		http.formLogin(form -> form
				.loginPage("/login")
				.permitAll());
		http.logout();
	    return http.build();
	}
	
	@Bean
 	public JdbcUserDetailsManager userDetailsService(DataSource datasource) {
 		UserDetails user = User.builder()
 			.username("user")
 			.password(bCryptPasswordEncoder.encode(""))
 			.roles("USER")
 			.build();
 		UserDetails admin = User.builder()
 			.username("admin")
 			.password(bCryptPasswordEncoder.encode(""))
 			.roles("ADMIN", "USER")
 			.build();
 		JdbcUserDetailsManager service = new JdbcUserDetailsManager(datasource);
 		//service.createUser(user);
 		//service.createUser(admin);
 		return service;
 	}
}
