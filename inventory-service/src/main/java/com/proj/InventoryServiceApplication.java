package com.proj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.proj.model.Inventory;
import com.proj.repo.InventoryRepository;

@SpringBootApplication
public class InventoryServiceApplication implements CommandLineRunner{

	@Autowired
	private InventoryRepository repo;
	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/*
		 * Inventory in = new Inventory(); in.setSkuCode("iphone_13");
		 * in.setQuantity(100);
		 * 
		 * Inventory inn = new Inventory(); inn.setSkuCode("iphone_13_red");
		 * inn.setQuantity(0);
		 * 
		 * repo.save(in); repo.save(inn);
		 */
	}

	
	
}
