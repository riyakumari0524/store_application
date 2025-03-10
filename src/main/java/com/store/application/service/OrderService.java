package com.store.application.service;

import com.store.application.dto.OrderResponse;
import com.store.application.entity.User;

public interface OrderService {

    OrderResponse checkout(User user);

}
