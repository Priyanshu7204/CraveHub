package com.example.demo.services;

import java.util.Date;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Orders;
import com.example.demo.repositories.OrderRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@Service
public class PaymentService {

	// Read Razorpay keys from application.properties
	@Value("${razorpay.key.id}")
	private String keyId;

	@Value("${razorpay.key.secret}")
	private String keySecret;

	@Autowired
	private OrderRepository orderRepository;

	// Method to create Razorpay Order
	public String createRazorpayOrder(int amount) throws Exception {
		RazorpayClient client = new RazorpayClient(keyId, keySecret);

		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", amount * 100);
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

		Order order = client.orders.create(orderRequest);
		return order.toString();
	}

	// Method to save payment details in database
	public void savePaymentDetails(Map<String, Object> data) throws Exception {
		String paymentId = data.get("payment_id").toString();
		String orderId = data.get("order_id").toString();
		String status = data.get("status").toString();

		Orders order = new Orders();
		order.setRazorpayOrderId(orderId);
		order.setPaymentId(paymentId);
		order.setPaymentStatus(status);
		order.setOrderDate(new Date());

		this.orderRepository.save(order);
	}
}