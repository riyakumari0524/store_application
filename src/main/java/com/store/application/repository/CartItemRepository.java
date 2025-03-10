package com.store.application.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.application.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
