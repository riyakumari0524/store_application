package com.store.application.service;

import com.store.application.dto.CartResponse;
import com.store.application.dto.UserCartResponse;
import com.store.application.entity.Cart;
import com.store.application.entity.User;

public interface CartService {

    UserCartResponse addItemToCart(User user, Long productId, Integer quantity);

    UserCartResponse modifyItemInCart(User user, Long cartItemId, Integer quantity);

    UserCartResponse removeItemFromCart(User user, Long cartItemId);

    CartResponse viewCartDetails(User user);
}

