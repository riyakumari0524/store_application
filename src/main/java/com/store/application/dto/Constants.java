package com.store.application.dto;

import java.math.RoundingMode;

public final class Constants {
    public static final String USER_ALREADY_EXISTS = "An account with this email already exists. Please log in.";
    public static final String INVALID_PASSWORD = "The password you entered is invalid.";
    public static final String USER_NOT_FOUND = "No account found with this email. Please register first.";

    // Error messages related to product and cart management
    public static final String PRODUCT_NOT_FOUND = "The requested product could not be found.";
    public static final String CART_NOT_FOUND = "No cart was found for this user.";
    public static final String CART_ITEM_NOT_FOUND = "The item was not found in your cart.";
    public static final String INSUFFICIENT_STOCK = "Sorry, there is not enough stock for product: ";

    // User session management
    public static final String USER_SESSION_ID = "userId";

    // Success messages
    public static final String PRODUCT_ADDED = "Product(s) successfully added to your cart.";
    public static final String PRODUCT_UPDATED = "Product quantity updated successfully in your cart.";

    // General error messages
    public static final String UNAUTHORIZED_ACCESS = "You do not have permission to access this resource. Please log in.";

    public static final int PRICE_SCALE = 2;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
}
