package com.store.application.service;

import java.util.List;

import com.store.application.dto.ProductRequest;
import com.store.application.dto.ProductResponse;
import com.store.application.entity.Cart;
import com.store.application.entity.Product;
import com.store.application.entity.User;

public interface ProductService {
    public List<ProductResponse> getAllProducts();

    public ProductResponse getProductById(Long id);

    public ProductResponse saveProduct(ProductRequest product);

    public ProductResponse updateProduct(Long id, ProductRequest product);

    public Cart getCartForUser(User user);

    public Product getProduct(Long productIds);
}
