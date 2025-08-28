package com.proj;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;
import com.proj.dto.ProductRequest;
import com.proj.repo.ProductRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static final MongoDBContainer mongoDBContainer = 
            new MongoDBContainer("mongo:6.0.8")
                    .withReuse(true); // test speed ke liye

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private ProductRepository repo;
    
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.database", () -> "testdb");
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void contextLoads() {
        System.out.println("MongoDB running at: " + mongoDBContainer.getReplicaSetUrl());
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest req = getProductRequest();
        String pRequestString = objectMapper.writeValueAsString(req);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pRequestString))
                .andExpect(status().isCreated());
        
        repo.findAll();
        Assertions.assertTrue(repo.findAll().size()==1);
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("i Phone 13")
                .description("i Phone 13")
                .price(BigDecimal.valueOf(1200))
                .build();
    }
}
