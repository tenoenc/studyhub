package com.teno.hexagonal.adapter.inbound.http;

import com.teno.hexagonal.domain.Order;
import com.teno.hexagonal.port.inbound.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> body) {
        try {
            String userId = (String) body.get("userId");
            String productId = (String) body.get("productId");
            int amount = (int) body.get("amount");
            Map<String, Object> result = orderService.createOrder(userId, productId, amount);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> getOrder(@PathVariable String id) {
        try {
            Order order = orderService.getOrder(id);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("order", order);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("orders", orders);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("{id}/pay")
    public ResponseEntity<Map<String, Object>> processPayment(@PathVariable String id) {
        try {
            Map<String, Object> result = orderService.processPayment(id);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping("{id}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable String id) {
        try {
            Map<String, Object> result = orderService.cancelOrder(id);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
}
