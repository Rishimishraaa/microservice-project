package com.proj.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.proj.dto.InventoryResponse;
import com.proj.dto.OrderRequest;
import com.proj.event.OrderPlacedEvent;
import com.proj.model.Order;
import com.proj.model.OrderLineItems;
import com.proj.repo.OrderRepository;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@Transactional
public class OrderService {
	
	@Autowired
	private OrderRepository repo;
	
	@Autowired
	private WebClient.Builder webClientBuilder;

	@Autowired
	private KafkaTemplate<String, OrderPlacedEvent> template;
	
	@Autowired
	private Tracer tracer;
	@Transactional
	public Mono<String> placeOrder(OrderRequest request) {
		
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
		
		Span name = tracer.nextSpan().name("Inventory Service Lookup");
		try(Tracer.SpanInScope spanInScop = tracer.withSpan(name.start())){
			return webClientBuilder.build().get()
					.uri("http://inventory-service/api/inventory",
							uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
					.retrieve()
					.bodyToMono(InventoryResponse[].class)
					.flatMapMany(Flux::fromArray)
					.all(InventoryResponse::isInStock)
					.flatMap(allInStock ->{
						if(allInStock) {
							template.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
							return Mono.fromCallable(()->{
								repo.save(order);
								log.info("Order {} placed successfully ",order.getId());
								return "Order placed successfully with ID : "+order.getId();
							});
						}else {
							return Mono.error(new IllegalArgumentException("Product not in stock"));
						}
					});
		}catch (Exception e) {
			throw new IllegalArgumentException("product is not in scoper"+e.getMessage());
		}
		finally {
			name.end();
		}
		// Calling inventory and place order if product in stock
		
		
	
		
		
		

	}
}
