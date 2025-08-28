package com.proj.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.proj.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{

}
