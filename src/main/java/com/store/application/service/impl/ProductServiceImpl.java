package com.store.application.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.store.application.entity.*;
import com.store.application.repository.*;
import com.store.application.service.*;
import com.store.application.dto.*;


@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository productRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        logger.info("Fetching all products from the database.");
        List<Product> products = productRepository.findAll();
        return products.stream()
            .map(product -> {
                ProductResponse productResponse = new ProductResponse();
                BeanUtils.copyProperties(product, productResponse);
                return productResponse;
            })
            .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(Long id) {
        logger.info("Fetching product by id: {}", id);

        Product product = productRepository.findById(id).orElse(null);
        ProductResponse response = new ProductResponse();
        if (product == null) {
            response.setMessage(Constants.PRODUCT_NOT_FOUND);
            logger.error("Product not found with id: {}", id);

            return response;
        }

        response.setTitle(product.getTitle());
        response.setAvailable(product.getAvailable());
        response.setPrice(product.getPrice());
        return response;
    }


    @Override
    public ProductResponse saveProduct(ProductRequest productRequest) {
        logger.info("Saving new product: {}", productRequest.getTitle());

        Product product = new Product();
        product.setId(productRequest.getId());
        product.setTitle(productRequest.getTitle());
        product.setAvailable(productRequest.getAvailable());
        product.setPrice(productRequest.getPrice());

        Product savedProduct = productRepository.save(product);
        ProductResponse response = new ProductResponse();
        response.setMessage(Constants.PRODUCT_ADDED);
        logger.debug("Product saved: {}", savedProduct.getTitle());

        return response;
    }


    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        logger.info("Updating product with id: {}", id);
        ProductResponse response = new ProductResponse();
        if (!productRepository.existsById(id)) {
            logger.error("Product not found with id: {}", id);
            response.setMessage(Constants.PRODUCT_NOT_FOUND);

            return response;
        }

        Product existingProduct = getProduct(id);
        existingProduct.setTitle(productRequest.getTitle());
        existingProduct.setAvailable(productRequest.getAvailable());
        existingProduct.setPrice(productRequest.getPrice());

        productRepository.save(existingProduct);
        response.setMessage(Constants.PRODUCT_UPDATED);
        logger.debug("Product updated: {}", existingProduct.getTitle());

        return response;
    }

    public Cart getCartForUser(User user) {
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException(Constants.CART_NOT_FOUND));
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException(Constants.PRODUCT_NOT_FOUND));
    }

}
