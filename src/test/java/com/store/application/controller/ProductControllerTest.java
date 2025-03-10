package com.store.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.store.application.dto.ProductRequest;
import com.store.application.dto.ProductResponse;
import com.store.application.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductResponse productResponse;
    private ProductRequest productRequest;

    @BeforeEach
    public void setUp() {
        productResponse = new ProductResponse();
        productResponse.setTitle("Product 1");
        productResponse.setAvailable(100);
        productResponse.setPrice(new BigDecimal("50.00"));

        productRequest = new ProductRequest();
        productRequest.setTitle("Product 1");
        productRequest.setAvailable(100);
        productRequest.setPrice(new BigDecimal("50.00"));
    }

    @Test
    void testGetAllProducts() {
        List<ProductResponse> products = Arrays.asList(productResponse);
        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<List<ProductResponse>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Product 1", response.getBody().get(0).getTitle());
        assertEquals(new BigDecimal("50.00"), response.getBody().get(0).getPrice());
    }

    @Test
    void testGetProductById() {
        Long productId = 1L;
        when(productService.getProductById(productId)).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.getProductById(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product 1", response.getBody().getTitle());
        assertEquals(new BigDecimal("50.00"), response.getBody().getPrice());
    }

    @Test
    void testSaveProduct() {
        when(productService.saveProduct(productRequest)).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.saveProduct(productRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Product 1", response.getBody().getTitle());
        assertEquals(new BigDecimal("50.00"), response.getBody().getPrice());
    }

    @Test
    void testUpdateProduct() {
        Long productId = 1L;
        when(productService.updateProduct(productId, productRequest)).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.updateProduct(productId, productRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product 1", response.getBody().getTitle());
        assertEquals(new BigDecimal("50.00"), response.getBody().getPrice());  // Verify price
    }
}
