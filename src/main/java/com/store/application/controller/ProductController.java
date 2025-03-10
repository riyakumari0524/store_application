package com.store.application.controller;

import java.util.List;

import com.store.application.entity.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.store.application.annotation.RoleRequired;
import com.store.application.dto.*;
import com.store.application.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all products", description = "This endpoint retrieves a list of all available products.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved all products"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "404", description = "No products found")
    })
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @Operation(summary = "Get product by id", description = "This endpoint retrieves specific available product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all products"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "404", description = "No products found")
    })
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @Operation(summary = "Save a new product", description = "This endpoint saves a new product with the provided details (title, available quantity, and price).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the product"),
            @ApiResponse(responseCode = "400", description = "Invalid product data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @RoleRequired(value = Role.ADMIN)
    @PostMapping("/products")
    public ResponseEntity<ProductResponse> saveProduct(@RequestBody ProductRequest product) {
        ProductResponse savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @Operation(summary = "Update an existing product", description = "This endpoint allows users with the 'ADMIN' role to update an existing product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the product"),
            @ApiResponse(responseCode = "400", description = "Invalid product data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @RoleRequired(value = Role.ADMIN)
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @RequestBody ProductRequest product) {
        ProductResponse updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }
}

