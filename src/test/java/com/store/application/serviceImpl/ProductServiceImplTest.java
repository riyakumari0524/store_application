package com.store.application.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import com.store.application.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.store.application.dto.Constants;
import com.store.application.dto.ProductRequest;
import com.store.application.entity.Cart;
import com.store.application.entity.Product;
import com.store.application.entity.User;
import com.store.application.repository.CartRepository;
import com.store.application.repository.ProductRepository;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequest productRequestDto;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setAvailable(10);
        product.setPrice(new BigDecimal("25.00"));

        productRequestDto = new ProductRequest();
        productRequestDto.setTitle("Test Product");
        productRequestDto.setAvailable(10);
        productRequestDto.setPrice(new BigDecimal("25.00"));

    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        var response = productService.getAllProducts();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Test Product", response.get(0).getTitle());
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        var response = productService.getProductById(1L);

        assertNotNull(response);
        assertEquals("Test Product", response.getTitle());
        assertEquals(10, response.getAvailable());
        assertEquals("25.00", response.getPrice().toString());
    }

    @Test
    void testGetProductById_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        var response = productService.getProductById(1L);

        assertNotNull(response);
        assertEquals(Constants.PRODUCT_NOT_FOUND, response.getMessage());
    }

    @Test
    void testSaveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        var response = productService.saveProduct(productRequestDto);

        assertNotNull(response);
        assertEquals(Constants.PRODUCT_ADDED, response.getMessage());
    }

    @Test
    void testUpdateProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productRequestDto.setTitle("Updated Product");
        productRequestDto.setAvailable(20);
        productRequestDto.setPrice(new BigDecimal("30.00"));

        var response = productService.updateProduct(1L, productRequestDto);

        assertNotNull(response);
        assertEquals(Constants.PRODUCT_UPDATED, response.getMessage());
        assertEquals("Updated Product", product.getTitle());
        assertEquals(20, product.getAvailable());
        assertEquals("30.00", product.getPrice().toString());
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        var response = productService.updateProduct(1L, productRequestDto);

        assertNotNull(response);
        assertEquals(Constants.PRODUCT_NOT_FOUND, response.getMessage());
    }

    @Test
    void testGetCartForUser_Success() {
        var user = new User();
        user.setId(1L);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(new Cart()));

        var cart = productService.getCartForUser(user);

        assertNotNull(cart);
        verify(cartRepository, times(1)).findByUser(user);
    }

    @Test
    void testGetCartForUser_CartNotFound() {
        var user = new User();
        user.setId(1L);
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            productService.getCartForUser(user);
        });

        assertEquals(Constants.CART_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testGetProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        var foundProduct = productService.getProduct(1L);

        assertNotNull(foundProduct);
        assertEquals("Test Product", foundProduct.getTitle());
        assertEquals(10, foundProduct.getAvailable());
        assertEquals("25.00", foundProduct.getPrice().toString());
    }

    @Test
    void testGetProduct_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            productService.getProduct(1L);
        });

        assertEquals(Constants.PRODUCT_NOT_FOUND, exception.getMessage());
    }
}
