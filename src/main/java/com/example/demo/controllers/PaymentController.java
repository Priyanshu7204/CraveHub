package com.example.demo.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	// Create order request
	@PostMapping("/create_order")
	public String createOrder(@RequestBody Map<String, Object> data) {
		try {
			int amt = Integer.parseInt(data.get("amount").toString());
			return paymentService.createRazorpayOrder(amt);
		} catch (Exception e) {
			System.out.println("Error creating Razorpay order !!");
			e.printStackTrace();
			return "Error generating order";
		}
	}

	// Update order request from frontend
	@PostMapping("/update_order")
	public ResponseEntity<?> updateOrderDetails(@RequestBody Map<String, Object> data) {
		try {

			paymentService.savePaymentDetails(data);
			
			System.out.println("Order saved successfully with Payment ID: " + data.get("payment_id"));
			return ResponseEntity.ok(Map.of("message", "Payment details saved successfully in database"));

		} catch (Exception e) {
			System.out.println("Error saving order to DB !!");
			e.printStackTrace(); // Simple printing instead of Logger
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Error saving order details"));
		}
	}
}