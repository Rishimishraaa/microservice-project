package com.proj.service;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proj.dto.ProductRequest;
import com.proj.dto.ProductResponse;
import com.proj.model.Product;
import com.proj.repo.ProductRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ProductService {

	@Autowired
	private ProductRepository repo;
	
	public void createProduct(ProductRequest request) {
		//BeanUtils.copyProperties(request, p); this is use copy one object into another object
		ModelMapper mapper = new ModelMapper();
		Product product = mapper.map(request, Product.class);
		
		Product save = repo.save(product);
		log.info("Product {} is saved",save.getId());
	}
	
	
	public List<ProductResponse> getAllProduct(){
		List<Product> all = repo.findAll();
		ModelMapper mapper = new ModelMapper();
		List<ProductResponse> list = all.stream()
				.map(product -> mapper.map(product, ProductResponse.class))
				.toList();
		
		return list;
	}
}
