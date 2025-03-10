package com.store.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.application.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
