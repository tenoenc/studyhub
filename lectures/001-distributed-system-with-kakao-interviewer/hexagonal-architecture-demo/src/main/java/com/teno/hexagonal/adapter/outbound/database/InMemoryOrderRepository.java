package com.teno.hexagonal.adapter.outbound.database;

import com.teno.hexagonal.domain.Order;
import com.teno.hexagonal.port.outbound.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryOrderRepository implements OrderRepository {
    Map<String, Order> orders;

    public InMemoryOrderRepository() {
        this.orders = new HashMap<>();
    }

    @Override
    public Order save(Order order) {
        orders.put(order.getId(), order);
        return order;
    }

    @Override
    public Order findById(String orderId) {
        return orders.get(orderId);
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public Order update(Order order) {
        if (!orders.containsKey(order.getId())) {
            throw new RuntimeException("주문을 찾을 수 없습니다");
        }
        orders.put(order.getId(), order);
        return order;
    }

    @Override
    public Order delete(String orderId) {
        return orders.remove(orderId);
    }
}
