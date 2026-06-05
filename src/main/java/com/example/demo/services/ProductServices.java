package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;

@Service
public class ProductServices {

	@Autowired
	private ProductRepository productRepository;

	public void addProduct(Product p) {
		this.productRepository.save(p);
	}

	public List<Product> getAllProducts() {
		return (List<Product>) this.productRepository.findAll();
	}

	public Product getProduct(int id) {
		Optional<Product> optional = this.productRepository.findById(id);
		return optional.orElse(null);
	}

	public void updateproduct(Product p, int id) {
		p.setPid(id);
		if (this.productRepository.findById(id).isPresent()) {
			this.productRepository.save(p);
		}
	}

	public void deleteProduct(int id) {
		this.productRepository.deleteById(id);
	}

	public List<Product> getProductByName(String name) {
		return this.productRepository.findByPnameContainingIgnoreCase(name);
	}
}