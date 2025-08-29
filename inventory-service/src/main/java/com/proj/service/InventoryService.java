package com.proj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proj.dto.InventoryResponse;
import com.proj.model.Inventory;
import com.proj.repo.InventoryRepository;

@Service
public class InventoryService {

	@Autowired
	private InventoryRepository repo;
	
	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStoke(List<String> skuCode) {
		return  repo.findBySkuCodeIn(skuCode).stream().map(in -> 
					InventoryResponse.builder()
					.skuCode(in.getSkuCode())
					.isInStock(in.getQuantity()>0)
					.build()
				).toList();
		
	}
}
