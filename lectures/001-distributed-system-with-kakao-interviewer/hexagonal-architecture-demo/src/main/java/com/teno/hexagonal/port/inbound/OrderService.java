package com.teno.hexagonal.port.inbound;

import com.teno.hexagonal.domain.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Map<String, Object> createOrder(String userId, String productId, int amount);

    Order getOrder(String orderId);

    List<Order> getAllOrders();

    Map<String, Object> processPayment(String orderId);

    Map<String, Object> cancelOrder(String orderId);
}
