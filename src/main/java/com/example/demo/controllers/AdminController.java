package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.AdminLogin;
import com.example.demo.services.AdminServices;
import com.example.demo.services.OrderServices;
import com.example.demo.services.ProductServices;
import com.example.demo.services.UserServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	@Autowired
	private UserServices userServices;
	@Autowired
	private AdminServices adminServices;
	@Autowired
	private ProductServices productServices;
	@Autowired
	private OrderServices orderServices;

	@PostMapping("/adminLogin")
	public String handleAdminLogin(@ModelAttribute("adminLogin") AdminLogin login, Model model, HttpSession session) {
		if (adminServices.validateAdminCredentials(login.getEmail(), login.getPassword())) {
			session.setAttribute("activeAdmin", login.getEmail());
			return "redirect:/admin/services";
		}
		model.addAttribute("error", "Invalid Admin Credentials");
		return "Login";
	}

	@GetMapping("/admin/services")
	public String renderAdminDashboard(Model model, HttpSession session) {
		if (session.getAttribute("activeAdmin") == null)
			return "redirect:/login";

		model.addAttribute("users", userServices.getAllUser());
		model.addAttribute("admins", adminServices.getAll());
		model.addAttribute("products", productServices.getAllProducts());
		model.addAttribute("orders", orderServices.getOrders());
		return "Admin_Page";
	}

	@GetMapping("/admin/logout")
	public String handleLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}

	@GetMapping("/admin/toggleUser/{id}")
	public String toggleAccountStatus(@PathVariable("id") int id, HttpSession session) {
		if (session.getAttribute("activeAdmin") == null)
			return "redirect:/login";
		userServices.toggleUserStatus(id);
		return "redirect:/admin/services";
	}

	@GetMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable("id") int id, HttpSession session) {
		if (session.getAttribute("activeAdmin") == null)
			return "redirect:/login";
		userServices.deleteUser(id);
		return "redirect:/admin/services";
	}

	@PostMapping("/admin/updateStatus")
	public String updateStatus(@RequestParam("orderId") int id, @RequestParam("newStatus") String status,
			HttpSession session) {
		if (session.getAttribute("activeAdmin") == null)
			return "redirect:/login";
		orderServices.updateOrderStatus(id, status);
		return "redirect:/admin/services#orders";
	}
}