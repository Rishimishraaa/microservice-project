package com.proj.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proj.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
