package com.teno.hexagonal.application;


import com.teno.hexagonal.domain.Order;
import com.teno.hexagonal.port.inbound.OrderService;
import com.teno.hexagonal.port.outbound.OrderRepository;
import com.teno.hexagonal.port.outbound.PaymentGateway;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private PaymentGateway paymentGateway;

    public OrderServiceImpl(OrderRepository orderRepository, PaymentGateway paymentGateway) {
        this.orderRepository = orderRepository;
        this.paymentGateway = paymentGateway;
    }

    @Override
    public Map<String, Object> createOrder(String userId, String productId, int amount) {
        if (userId == null || productId == null || amount <= 0) {
            throw new RuntimeException("유효하지 않은 주문 정보입니다");
        }

        String orderId = String.valueOf(new Date().getTime());
        Order order = new Order(orderId, userId, productId, amount);

        orderRepository.save(order);

        Map<String, Object> ret = new HashMap<>();
        Map<String, Object> ret2 = new HashMap<>();
        ret.put("success", true);
        ret.put("message", "주문이 생성되었습니다.");
        ret2.put("id", order.getId());
        ret2.put("userId", order.getUserId());
        ret2.put("productId", order.getProductId());
        ret2.put("amount", order.getAmount());
        ret2.put("status", order.getStatus());
        ret.put("order", ret2);

        return ret;
    }

    @Override
    public Order getOrder(String orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new RuntimeException("주문을 찾을 수 없습니다");
        }
        return order;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders;
    }

    @Override
    public Map<String, Object> processPayment(String orderId) {
        Order order = orderRepository.findById(orderId);

        if (order == null) {
            throw new RuntimeException("주문을 찾을 수 없습니다");
        }

        if (!order.canBePaid()) {
            throw new RuntimeException("결제할 수 없는 주문입니다");
        }

        try {
            Map<String, Object> paymentResult = paymentGateway.processPayment(
                    order.getId(),
                    order.getAmount()
            );

            boolean success = (Boolean) paymentResult.get("success");
            if (success) {
                order.markAsPaid();

                orderRepository.update(order);

                Map<String, Object> ret = new HashMap<>();
                Map<String, Object> ret2 = new HashMap<>();
                ret.put("success", true);
                ret.put("message", "결제가 완료되었습니다");
                ret2.put("id", order.getId());
                ret2.put("status", order.getStatus());
                ret2.put("amount", order.getAmount());
                ret.put("order", ret2);

                return ret;
            } else {
                String reason = (String) paymentResult.get("reason");
                order.markAsFailed(reason);
                orderRepository.update(order);

                throw new RuntimeException("결제 실패: " + reason);
            }
        } catch (Exception e) {
            order.markAsFailed(e.getMessage());
            orderRepository.update(order);
            throw e;
        }
    }

    @Override
    public Map<String, Object> cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId);

        if (order == null) {
            throw new RuntimeException("주문을 찾을 수 없습니다");
        }

        order.cancel();

        orderRepository.update(order);

        Map<String, Object> ret = new HashMap<>();
        Map<String, Object> ret2 = new HashMap<>();
        ret.put("success", true);
        ret.put("message", "주문이 취소되었습니다");
        ret2.put("id", order.getId());
        ret2.put("status", order.getStatus());
        ret.put("order", ret2);

        return ret;
    }
}
