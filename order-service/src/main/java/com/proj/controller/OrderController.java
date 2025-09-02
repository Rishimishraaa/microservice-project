package com.proj.controller;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.discovery.converters.Auto;
import com.proj.dto.OrderRequest;
import com.proj.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.tracing.Tracer;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	

	
	ExecutorService executor = Executors.newFixedThreadPool(10);
	
	@PostMapping
	@Retry(name = "inventory", fallbackMethod = "fallbackMethod")
	@CircuitBreaker(name = "inventory",fallbackMethod = "fallbackMethod")
	public Mono<ResponseEntity<String>> placeOrder(@RequestBody OrderRequest request){
		
		return orderService.placeOrder(request)
				.timeout(Duration.ofSeconds(3))
				.map(ResponseEntity::ok)
				.onErrorResume(TimeoutException.class, ex->
					Mono.just(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Request time out, please try again later!"))
					
						);
	}
	
	public Mono<ResponseEntity<String>> fallbackMethod(OrderRequest orderRequest, Throwable e){
		return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Order service is currently unvailable. Please try again later!"));
				
				
	}
}
