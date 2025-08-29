package com.proj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proj.dto.InventoryResponse;
import com.proj.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	
	@Autowired
	private InventoryService service;

	@GetMapping
	public ResponseEntity<?> isInStoke(@RequestParam List<String> skuCode){
		 List<InventoryResponse> inStoke = service.isInStoke(skuCode);
		return ResponseEntity.ok(inStoke);
	}
}
