package com.example.demo.services;

import java.util.Date;
import java.util.List;
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Orders;
import com.example.demo.entities.User;
import com.example.demo.repositories.OrderRepository;

@Service
public class OrderServices {
	
	@Autowired
	private OrderRepository orderRepository;

	// Get all orders for admin dashboard
	public List<Orders> getOrders() {
		return this.orderRepository.findAll();
	}

	// Update order status from admin panel
	public void updateOrderStatus(int orderId, String newStatus) {
		Optional<Orders> optional = orderRepository.findById(orderId);
		if (optional.isPresent()) {
			Orders order = optional.get();
			order.setStatus(newStatus); // Update new status
			orderRepository.save(order); // Save to database
		}
	}

	// Calculate total price for a single item (Moved from Controller)
	public double calculateSingleOrderTotal(Orders order) {
		double total = 0.0;
		if (order.getoQuantity() > 0) {
			total = order.getoPrice() * order.getoQuantity();
		}
		return total;
	}

	// Calculate total price for multiple items using basic loop (Moved from Controller)
	public double calculateBulkOrderTotal(List<Orders> orders) {
		double total = 0.0;
		for (Orders o : orders) {
			if (o.getoQuantity() > 0) {
				total = total + (o.getoPrice() * o.getoQuantity());
			}
		}
		return total;
	}

	// Process and save order details after successful payment
	public void processAndSaveOrder(Orders order, User user, String paymentId, String razorpayOrderId) {
		if (order != null) {
			order.setUser(user);
			order.setOrderDate(new Date());
			order.setPaymentId(paymentId);
			order.setRazorpayOrderId(razorpayOrderId);
			order.setPaymentStatus("Paid");
			order.setStatus("Pending");

			// Calculate total amount if it is 0
			if (order.getTotalAmmout() == 0) {
				order.setTotalAmmout(calculateSingleOrderTotal(order));
			}

			this.orderRepository.save(order);
		}
	}

	// Process bulk orders using simple for-loop (Removed Stream API)
	public void processBulkOrders(List<Orders> orders, User user, String pId, String rId) {
		for (Orders o : orders) {
			if (o.getoQuantity() > 0) {
				processAndSaveOrder(o, user, pId, rId);
			}
		}
	}

	// Save new order
	public void saveOrder(Orders order) {
		this.orderRepository.save(order);
	}

	// Update existing order
	public void updateOrder(int id, Orders order) {
		order.setoId(id);
		this.orderRepository.save(order);
	}

	// Delete order by ID
	public void deleteOrder(int id) {
		this.orderRepository.deleteById(id);
	}

	// Get all orders for a specific user
	public List<Orders> getOrdersForUser(User user) {
		return this.orderRepository.findOrdersByUser(user);
	}
}