package com.vms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new CustomeUserDetailsService();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable().authorizeHttpRequests()
		.requestMatchers("/")
		.hasRole("ADMIN")

		.anyRequest()
		.authenticated()
		.and()
		.formLogin().loginPage("/signin")
		.loginProcessingUrl("/login")
		.defaultSuccessUrl("/")
		.permitAll();
		
		return httpSecurity.build();
	}
	
//	.requestMatchers("/user")
//	.hasRole("USER")
//	.requestMatchers("/")
//	.permitAll()
	
	
	
	
	
	
//	@Bean
//	UserDetailsService userDetailsService() {
//		UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("1234")).roles("ADMIN").build();
//		InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager(admin);
////		System.out.println("Password is : "+admin.getPassword());
////		System.out.println("Username is : "+admin.getUsername());
////		System.out.println("Autherity is : "+admin.getAuthorities());
//		return detailsManager;
//	}
//	
//	@Bean
//	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//		httpSecurity.csrf().disable().authorizeHttpRequests()
//		.requestMatchers("/**")
//		.hasRole("ADMIN")
//		.anyRequest()
//		.authenticated()
//		.and()
//		.formLogin();
//		
//		return httpSecurity.build();
//	}
}
