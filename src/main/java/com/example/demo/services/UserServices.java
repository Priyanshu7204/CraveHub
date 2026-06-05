package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.Random; // <-- Ye naya import OTP ke liye add kiya hai

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;

@Service
public class UserServices {

	@Autowired
	private UserRepository userRepository;

	public List<User> getAllUser() {
		return (List<User>) this.userRepository.findAll();
	}

	public User getUser(int id) {
		Optional<User> optional = this.userRepository.findById(id);
		return optional.orElse(null);
	}

	public User getUserByEmail(String email) {
		return this.userRepository.findUserByUemail(email);
	}

	public void deleteUser(int id) {
		this.userRepository.deleteById(id);
	}

	public void addUser(User user) {
		this.userRepository.save(user);
	}

	public void toggleUserStatus(int id) {
		Optional<User> optional = this.userRepository.findById(id);
		if (optional.isPresent()) {
			User user = optional.get();
			user.setActive(!user.isActive());
			this.userRepository.save(user);
		}
	}

	public boolean validateLoginCredentials(String email, String password) {
		User user = this.userRepository.findUserByUemail(email);
		return user != null && user.getUpassword() != null && user.getUpassword().equals(password);
	}
	
	// OTP generate karne ka naya method (Controller se yahan move kiya)
	public String generateOtp() {
		Random random = new Random();
		int otpNum = random.nextInt(999999);
		return String.format("%06d", otpNum);
	}
}