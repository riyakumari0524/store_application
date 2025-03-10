package com.store.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.application.entity.Cart;
import com.store.application.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

}

