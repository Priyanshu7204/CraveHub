package com.example.demo.dto;

import java.util.List;
import com.example.demo.entities.Orders;

public class OrderWrapper {
	private List<Orders> orders;

	public List<Orders> getOrders() {
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}
}