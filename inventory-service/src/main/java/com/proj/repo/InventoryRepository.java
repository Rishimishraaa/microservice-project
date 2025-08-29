package com.proj.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proj.model.Inventory;


public interface InventoryRepository extends JpaRepository<Inventory, Long>{

	public Optional<Inventory> findBySkuCode(String skuCode);

	public List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
