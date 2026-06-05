package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.OrderWrapper;
import com.example.demo.entities.Orders;
import com.example.demo.entities.User;
import com.example.demo.services.OrderServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

	@Autowired
	private OrderServices orderServices;

	// Process single product order
	@PostMapping("/product/order")
	public String initateSingleOrder(@ModelAttribute Orders order, HttpSession session) {
		if (session.getAttribute("activeUser") == null)
			return "redirect:/login";

		// Calculate total amount from service layer
		double total = this.orderServices.calculateSingleOrderTotal(order);
		
		session.setAttribute("pendingOrder", order);
		session.setAttribute("pendingOrderType", "SINGLE");
		session.setAttribute("finalAmount", total);
		return "redirect:/payment";
	}

	// Process bulk orders from cart
	@PostMapping("/product/orderAll")
	public String initiateBulkOrder(@ModelAttribute OrderWrapper form, HttpSession session) {
		if (session.getAttribute("activeUser") == null)
			return "redirect:/login";

		// Calculate total amount for all items from service layer
		double total = this.orderServices.calculateBulkOrderTotal(form.getOrders());

		session.setAttribute("pendingBulkOrder", form);
		session.setAttribute("pendingOrderType", "BULK");
		session.setAttribute("finalAmount", total);
		return "redirect:/payment";
	}

	// Display payment checkout page
	@GetMapping("/payment")
	public String renderPaymentGateway(Model model, HttpSession session) {
		Double amount = (Double) session.getAttribute("finalAmount");
		if (amount == null)
			return "redirect:/product/back";

		model.addAttribute("amount", amount);
		return "RazorpayCheckout";
	}

	// Save transaction details after successful payment
	@PostMapping("/processPayment")
	public String finalizeTransaction(@RequestParam("payment_id") String pId, @RequestParam("order_id") String rId,
			HttpSession session) {
		User user = (User) session.getAttribute("activeUser");
		String type = (String) session.getAttribute("pendingOrderType");

		if ("SINGLE".equals(type)) {
			Orders order = (Orders) session.getAttribute("pendingOrder");
			orderServices.processAndSaveOrder(order, user, pId, rId);
		} else {
			OrderWrapper form = (OrderWrapper) session.getAttribute("pendingBulkOrder");
			orderServices.processBulkOrders(form.getOrders(), user, pId, rId);
		}

		// Clear temporary session data
		session.removeAttribute("pendingOrder");
		session.removeAttribute("pendingBulkOrder");
		session.removeAttribute("finalAmount");
		session.removeAttribute("pendingOrderType");

		return "redirect:/product/back";
	}
}