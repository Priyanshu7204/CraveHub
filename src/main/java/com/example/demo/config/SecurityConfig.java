package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())

				.authorizeHttpRequests(auth -> auth.requestMatchers(
						// Static Resources & Public Pages
						"/", "/home", "/products", "/about", "/location", "/css/**", "/Images/**", "/JavaScript/**",

						// Authentication & OTP Routes
						"/register", "/addingUser", "/login", "/userLogin", "/adminLogin", "/verifyOtpPage",
						"/verifyOtpProcess",

						// Admin Routes
						"/admin/**", "/deleteUser/**", "/addProduct", "/addingProduct", "/updateProduct/**",
						"/updatingProduct/**", "/deleteProduct/**",

						// User Dashboard & Payment Routes
						"/product/**", "/payment/**", "/processPayment", "/completeProfile", "/saveGoogleUser",

						// Error Route
						"/error").permitAll()

						.anyRequest().authenticated())

				.formLogin(form -> form.loginPage("/login").permitAll())

				.oauth2Login(oauth2 -> oauth2.loginPage("/login").defaultSuccessUrl("/product/back", true));

		return http.build();
	}
}