package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.services.OrderServices;
import com.example.demo.services.ProductServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

	@Autowired
	private ProductServices productServices;
	@Autowired
	private OrderServices orderServices;

	// --- ADMIN: PRODUCT MANAGEMENT ---

	@GetMapping("/addProduct")
	public String openAddProductPage(HttpSession session) {
		if (session.getAttribute("activeAdmin") == null) {
			return "redirect:/login";
		}
		return "Add_Product";
	}

	@PostMapping("/addingProduct")
	public String addProduct(@ModelAttribute Product product, HttpSession session) {
		if (session.getAttribute("activeAdmin") == null) {
			return "redirect:/login";
		}
		try {
			this.productServices.addProduct(product);
		} catch (Exception e) {
			System.out.println("Error occurred while adding product: " + e.getMessage());
			e.printStackTrace();
		}
		return "redirect:/admin/services";
	}

	@GetMapping("/updateProduct/{productId}")
	public String openUpdatePage(@PathVariable("productId") int id, Model model, HttpSession session) {
		if (session.getAttribute("activeAdmin") == null) {
			return "redirect:/login";
		}
		Product product = this.productServices.getProduct(id);
		model.addAttribute("product", product);
		return "Update_Product";
	}

	@PostMapping("/updatingProduct/{productId}")
	public String updateProduct(@ModelAttribute Product product, @PathVariable("productId") int id,
			HttpSession session) {
		if (session.getAttribute("activeAdmin") == null) {
			return "redirect:/login";
		}
		this.productServices.updateproduct(product, id);
		return "redirect:/admin/services";
	}

	@GetMapping("/deleteProduct/{productId}")
	public String delete(@PathVariable("productId") int id, HttpSession session) {
		if (session.getAttribute("activeAdmin") == null) {
			return "redirect:/login";
		}
		this.productServices.deleteProduct(id);
		return "redirect:/admin/services";
	}

	// --- USER: PRODUCT SEARCH ---
	@PostMapping("/product/search")
	public String handleProductSearch(@RequestParam("productName") String name, Model model, HttpSession session) {
		User user = (User) session.getAttribute("activeUser");
		if (user == null) {
			return "redirect:/login";
		}

		List<Product> products = productServices.getProductByName(name);
		model.addAttribute("orders", orderServices.getOrdersForUser(user));
		model.addAttribute("name", user.getUname());

		if (products.isEmpty()) {
			model.addAttribute("msg", "Item not found in our kitchen!");
		} else {
			model.addAttribute("products", products);
		}
		return "BuyProduct";
	}
}