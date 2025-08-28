package com.proj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proj.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	
	@Autowired
	private InventoryService service;

	@GetMapping("/{sku-code}")
	public ResponseEntity<?> isInStoke(@PathVariable("sku-code") String skucode){
		boolean inStoke = service.isInStoke(skucode);
		return ResponseEntity.ok(inStoke);
	}
}
