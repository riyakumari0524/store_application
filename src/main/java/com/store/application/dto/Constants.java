package com.store.application.dto;

import java.math.RoundingMode;

public final class Constants {
    public static final String USER_ALREADY_EXISTS = "Email already exists. Please login.";
    public static final String INCORRECT_PASSWORD = "Incorrect password";
    public static final String USER_NOT_FOUND = "Email does not exist. Please register first.";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String CART_NOT_FOUND = "Cart not found";
    public static final String CART_ITEM_NOT_FOUND = "Cart item not found";
    public static final String INSUFFICIENT_STOCK = "Insufficient stock for product: ";
    public static final String USER_ID_SESSION_ATTRIBUTE = "userId";
    public static final String PRODUCT_ADDED = "Products added Successfully";
    public static final String PRODUCT_UPDATED = "Product updated Successfully";
    public static final String UNAUTHORIZED_ACCESS_MESSAGE = "You do not have permission to access this resource";
    public static final int PRICE_SCALE = 2;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
}
