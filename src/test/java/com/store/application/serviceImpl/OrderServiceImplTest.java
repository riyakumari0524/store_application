package com.store.application.serviceImpl;

import com.store.application.dto.Constants;
import com.store.application.dto.OrderResponse;
import com.store.application.entity.*;
import com.store.application.exception.NotFoundException;
import com.store.application.repository.CartRepository;
import com.store.application.repository.OrderRepository;
import com.store.application.repository.ProductRepository;
import com.store.application.service.ProductService;
import com.store.application.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    private User user;
    private Cart cart;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    public void setUp() {
        // Set up the user and cart
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);  // Associate the user with the cart
        user.setCart(cart); // Associate the user with the cart

        // Set up the product
        product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(new BigDecimal("100"));
        product.setAvailable(10);

        // Set up the CartItem
        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        // Add CartItem to the Cart
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        cart.setItems(new ArrayList<>(cartItems));

        // Mock the productService to return the cart when called
        when(productService.getCartForUser(user)).thenReturn(cart);
    }

    @Test
    public void testCheckout_CartIsEmpty() {
        cart.setItems(new ArrayList<>());
        when(productService.getCartForUser(user)).thenReturn(cart);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            orderServiceImpl.checkout(user);
        });

        assertEquals(Constants.CART_ITEM_NOT_FOUND, exception.getMessage());
    }


    @Test
    public void testCheckout_SuccessfulCheckout() {
        // Mock productRepository save behavior
        when(productRepository.save(product)).thenReturn(product);

        // Call the checkout method
        OrderResponse response = orderServiceImpl.checkout(user);

        // Assertions to validate the response
        assertNotNull(response);
        assertEquals(user.getId(), response.getUserId());
        assertEquals(user.getEmail(), response.getEmail());
        assertTrue(response.getTotalAmount().compareTo(BigDecimal.ZERO) > 0);

        // Verify interactions with repositories
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartRepository, times(1)).delete(cart);
        verify(productRepository, times(1)).save(product);
    }


}
