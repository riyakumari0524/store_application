package com.store.application.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.store.application.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.store.application.exception.InsufficientStockException;
import com.store.application.dto.Constants;
import com.store.application.dto.OrderResponse;
import com.store.application.entity.*;
import com.store.application.repository.*;
import com.store.application.service.*;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository,
                            ProductRepository productRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.productService = productService;

    }

    @Override
    public OrderResponse checkout(User user) {
        logger.info("Checking out cart for user {}", user.getId());

        if (user.getCart() == null) {
            logger.error("No cart for user {}", user.getId());
            throw new NotFoundException(Constants.CART_NOT_FOUND);
        }

        Cart cart = productService.getCartForUser(user);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            logger.warn("Checkout failed: Cart is empty or not found for user ID {}", user.getId());
            throw new NotFoundException(Constants.CART_ITEM_NOT_FOUND);
        }

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (product.getAvailable() < cartItem.getQuantity()) {
                logger.error("Insufficient stock for product {}. Requested: {}, Available: {}",
                        product.getTitle(), cartItem.getQuantity(), product.getAvailable());
                throw new InsufficientStockException(Constants.INSUFFICIENT_STOCK);
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        BigDecimal totalAmount = BigDecimal.ZERO.setScale(Constants.PRICE_SCALE, Constants.ROUNDING_MODE);

        Set<Product> products = new HashSet<>();

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            BigDecimal productTotal = product.getPrice().multiply(new BigDecimal(cartItem.getQuantity()))
                    .setScale(Constants.PRICE_SCALE, Constants.ROUNDING_MODE);
            totalAmount = totalAmount.add(productTotal);

            product.setAvailable(product.getAvailable() - cartItem.getQuantity());
            if (product.getAvailable() == 0) {
                product.setPrice(BigDecimal.ZERO);
            }

            products.add(product);
            productRepository.save(product);

            logger.info("Updated product {} after checkout. Remaining stock: {}", product.getTitle(),
                    product.getAvailable());
        }

        cart.getItems().clear();
        cartRepository.delete(cart);

        order.setTotalAmount(totalAmount);
        order.setProducts(new ArrayList<>(products));
        orderRepository.save(order);
        OrderResponse response = new OrderResponse();
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setOrderId(order.getId());
        response.setTotalAmount(totalAmount);


        logger.info("Order successfully created for user {}. Order ID: {}, Total Amount: {}", user.getId(),
                order.getId(), totalAmount);

        return response;
    }

}
