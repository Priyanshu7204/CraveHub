package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Admin;
import com.example.demo.repositories.AdminRepository;

@Service
public class AdminServices {

	@Autowired
	private AdminRepository adminRepository;

	public List<Admin> getAll() {
		return (List<Admin>) this.adminRepository.findAll();
	}

	public Admin getAdmin(int id) {
		return this.adminRepository.findById(id).orElse(null);
	}

	public void update(Admin admin, int id) {
		admin.setAdminId(id);
		this.adminRepository.save(admin);
	}

	public void delete(int id) {
		this.adminRepository.deleteById(id);
	}

	public void addAdmin(Admin admin) {
		this.adminRepository.save(admin);
	}

	public boolean validateAdminCredentials(String email, String password) {
		Admin admin = adminRepository.findByAdminEmail(email);
		return admin != null && admin.getAdminPassword().equals(password);
	}
}