package com.proj.service;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proj.dto.OrderRequest;
import com.proj.model.Order;
import com.proj.model.OrderLineItems;
import com.proj.repo.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class OrderService {
	
	@Autowired
	private OrderRepository repo;

	
	public void placeOrder(OrderRequest request) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		ModelMapper mapper = new ModelMapper();
		
		List<OrderLineItems> list = request.getOrderLineItemsDtoList()
		.stream()
		.map(OrderLineItemsDto -> mapper.map(OrderLineItemsDto, OrderLineItems.class)).toList();
		
		for(OrderLineItems items : list) {
			items.setOrder(order);
		}
		
		order.setOrderLineItemsList(list);
		repo.save(order);
		log.info("order ID :{} placed successfully",order.getId());
	}
}
