package com.proj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proj.dto.ProductRequest;
import com.proj.dto.ProductResponse;
import com.proj.service.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	private ProductService service;
	
	
	@PostMapping
	public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {
		 service.createProduct(request);
		 return ResponseEntity.created(null).body("Product created successfully...");
	 }
	 
	@GetMapping
	public ResponseEntity<?> getAllProducts(){
		List<ProductResponse> allProduct = service.getAllProduct();
		return ResponseEntity.ok(allProduct);
	}
	 
}
