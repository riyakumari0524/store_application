package com.store.application.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.*;

import com.store.application.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.store.application.dto.*;
import com.store.application.entity.*;
import com.store.application.exception.NotFoundException;
import com.store.application.repository.*;
import com.store.application.service.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal("100.00"));

        cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
    }

    @Test
    void testAddItemToCart_NewCartItem() {
        when(productService.getProduct(1L)).thenReturn(product);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        UserCartResponse response = cartService.addItemToCart(user, 1L, 2);

        assertNotNull(response);
        assertEquals(user.getId(), response.getUserId());
        verify(cartRepository, times(1)).save(cart);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testRemoveItemFromCart_ItemExists() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cart.getItems().add(cartItem);

        when(productService.getCartForUser(user)).thenReturn(cart);

        UserCartResponse response = cartService.removeItemFromCart(user, 1L);

        assertNotNull(response);
        assertEquals(user.getId(), response.getUserId());
        verify(cartItemRepository, times(1)).delete(cartItem);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testRemoveItemFromCart_ItemNotFound() {
        when(productService.getCartForUser(user)).thenReturn(cart);
        assertThrows(NotFoundException.class, () -> cartService.removeItemFromCart(user, 1L));
    }

    @Test
    void testModifyItemInCart_ItemNotFound() {
        when(productService.getCartForUser(user)).thenReturn(cart);
        assertThrows(NotFoundException.class, () -> cartService.modifyItemInCart(user, 1L, 5));
    }

    @Test
    void testViewCartDetails_EmptyCart() {
        when(productService.getCartForUser(user)).thenReturn(cart);

        CartResponse response = cartService.viewCartDetails(user);

        assertNotNull(response);
        assertTrue(response.getCartItems().isEmpty());
        assertEquals(0, response.getTotal().compareTo(BigDecimal.ZERO));
    }
}
