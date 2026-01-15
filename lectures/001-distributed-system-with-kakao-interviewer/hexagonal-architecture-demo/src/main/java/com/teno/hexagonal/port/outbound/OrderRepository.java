package com.teno.hexagonal.port.outbound;

import com.teno.hexagonal.domain.Order;

import java.util.List;

public interface OrderRepository {
    Order save(Order order);

    Order findById(String orderId);

    List<Order> findAll();

    Order update(Order order);

    Order delete(String orderId);
}
