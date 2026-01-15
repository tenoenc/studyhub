package com.teno.hexagonal.port.outbound;

import java.util.Map;

public interface PaymentGateway {
    Map<String, Object> processPayment(String orderId, int amount);

    Map<String, Object> refundPayment(String orderId);

    String getPaymentStatus(String orderId);
}
