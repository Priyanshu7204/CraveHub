package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendOtpEmail(String toEmail, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject("🍔 CraveHub - Verify Your Account (OTP)");
		message.setText("Welcome to CraveHub!\n\n" + "Your OTP for account verification is: " + otp + "\n\n"
				+ "Please enter this code on the website to complete your registration.\n\n" + "Enjoy your meal!");

		mailSender.send(message);
	}
}