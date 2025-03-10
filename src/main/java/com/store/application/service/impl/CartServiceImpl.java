package com.store.application.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.store.application.exception.*;
import com.store.application.entity.*;
import com.store.application.dto.*;
import com.store.application.repository.*;
import com.store.application.service.*;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
                           ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;

    }

    @Override
    public UserCartResponse addItemToCart(User user, Long productId, Integer quantity) {
        logger.info("Adding product with id {} and quantity {} to the cart for user {}", productId, quantity);
        Product product = productService.getProduct(productId);
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setItems(new ArrayList<>());
            logger.info("Creating a new cart for user {}", user.getId());
            return cartRepository.save(newCart);
        });

        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> Objects.equals(product.getId(), item.getProduct().getId())).findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setSubtotal(
                    existingItem.getProduct().getPrice().multiply(new BigDecimal(existingItem.getQuantity())));
            cartItemRepository.save(existingItem);
            logger.info("Updated quantity of product {} in the cart", productId);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            newItem.setSubtotal(product.getPrice().multiply(new BigDecimal(quantity)));
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
            logger.info("Added new product {} to the cart for user {}", productId, user.getId());
        }
        cartRepository.save(cart);
        UserCartResponse response = new UserCartResponse();
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());

        return response;
    }

    @Override
    public UserCartResponse removeItemFromCart(User user, Long cartItemId) {
        logger.info("Removing item with id {} from the cart for user {}", cartItemId, user.getId());
        Cart cart = productService.getCartForUser(user);
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> Objects.equals(cartItemId, item.getId())).findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            cart.getItems().remove(existingItem);
            cartItemRepository.delete(existingItem);
            cartRepository.save(cart);

            logger.info("Item with id {} removed from the cart for user {}", cartItemId, user.getId());
            UserCartResponse response = new UserCartResponse();
            response.setUserId(user.getId());
            response.setEmail(user.getEmail());

            return response;
        } else {
            logger.error("Cart item with id {} not found for user {}", cartItemId, user.getId());
            throw new NotFoundException(Constants.CART_ITEM_NOT_FOUND);
        }
    }

    @Override
    public UserCartResponse modifyItemInCart(User user, Long cartItemId, Integer quantity) {
        logger.info("Modifying quantity of item with id {} in the cart for user {}", cartItemId, user.getId());
        Cart cart = productService.getCartForUser(user);
        Optional<CartItem> cartItemOpt = cart.getItems().stream()
                .filter(item -> Objects.equals(cartItemId, item.getId())).findFirst();

        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();
            cartItem.setQuantity(quantity);
            cartItem.setSubtotal(cartItem.getProduct().getPrice().multiply(new BigDecimal(quantity)));
            cartItemRepository.save(cartItem);
            cartRepository.save(cart);

            logger.info("Updated quantity for cart item with id {} to {} for user {}", cartItemId, quantity,
                    user.getId());

            UserCartResponse response = new UserCartResponse();
            response.setUserId(user.getId());
            response.setEmail(user.getEmail());

            return response;
        } else {
            logger.error("Cart item with id {} not found for user {}", cartItemId, user.getId());
            throw new NotFoundException(Constants.CART_ITEM_NOT_FOUND);
        }
    }

    @Override
    public CartResponse viewCartDetails(User user) {
        logger.info("Fetching cart details for user {}", user.getId());
        Cart cart = productService.getCartForUser(user);
        List<CartItem> cartItems = cart.getItems();

        if (cartItems.isEmpty()) {
            logger.info("Cart is empty for user {}", user.getId());
            CartResponse response = new CartResponse();
            response.setCartItems(new ArrayList<>());
            response.setTotal(BigDecimal.ZERO.setScale(Constants.PRICE_SCALE, Constants.ROUNDING_MODE));
            return response;
        }

        BigDecimal totalPrice = BigDecimal.ZERO.setScale(Constants.PRICE_SCALE, Constants.ROUNDING_MODE);
        int ordinal = 1;
        List<CartItemDetails> cartItemList = new ArrayList<>();

        for (CartItem item : cartItems) {
            BigDecimal price = item.getProduct().getPrice();
            BigDecimal quantity = new BigDecimal(item.getQuantity());
            BigDecimal subtotal = price.multiply(quantity).setScale(Constants.PRICE_SCALE, Constants.ROUNDING_MODE);
            item.setSubtotal(subtotal);
            CartItemDetails itemResponse = new CartItemDetails(ordinal, item.getProduct().getTitle(), price,
                    item.getQuantity(), subtotal);

            cartItemList.add(itemResponse);
            totalPrice = totalPrice.add(subtotal);
            ordinal++;
        }

        totalPrice = totalPrice.setScale(Constants.PRICE_SCALE, Constants.ROUNDING_MODE);
        CartResponse response = new CartResponse();
        response.setCartItems(cartItemList);
        response.setTotal(totalPrice);

        logger.info("Successfully fetched cart details for user {}. Total: {}", user.getId(), totalPrice);
        return response;
    }

}
