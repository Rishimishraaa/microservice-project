package com.proj.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.proj.dto.InventoryResponse;
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
	
	@Autowired
	private WebClient.Builder webClientBuilder;

	
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
		
		List<String> skuCodes= order.getOrderLineItemsList()
		.stream().map(orderItem -> orderItem.getSkuCode())
		.toList();
		
		
		// Calling inventory and place order if product in stock
		
		 InventoryResponse[] responses = webClientBuilder.build().get()
		.uri("http://inventory-service/api/inventory", uriBuulder -> uriBuulder
				.queryParam("skuCode",skuCodes)
				.build())
		.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block();

		 boolean allProductsInStock = Arrays.stream(responses)
				 .allMatch(inventoryResponse -> inventoryResponse.isInStock());
		 
		
		if(allProductsInStock) {
			repo.save(order);
		}else {
			throw new IllegalArgumentException("product is not in stock, please try again later");
		}
		
		log.info("order ID :{} placed successfully",order.getId());
	}
}
