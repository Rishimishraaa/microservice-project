package com.proj.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proj.model.Inventory;
import com.proj.repo.InventoryRepository;

@Service
public class InventoryService {

	@Autowired
	private InventoryRepository repo;
	
	@Transactional(readOnly = true)
	public boolean isInStoke(String skuCode) {
		Optional<Inventory> bySkuCode = repo.findBySkuCode(skuCode);
		if(bySkuCode.isPresent()) {
			return true;
		}
		return false;
	}
}
