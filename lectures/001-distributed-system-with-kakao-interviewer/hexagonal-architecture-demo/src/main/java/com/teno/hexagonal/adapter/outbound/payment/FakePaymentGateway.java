package com.teno.hexagonal.adapter.outbound.payment;

import com.teno.hexagonal.port.outbound.PaymentGateway;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class FakePaymentGateway implements PaymentGateway {
    private Map<String, Object> payments;

    public FakePaymentGateway() {
        this.payments = new HashMap<>();
    }

    @Override
    public Map<String, Object> processPayment(String orderId, int amount) {
        boolean isSuccess = Math.random() > 0.1;

        if (isSuccess) {
            Map<String, Object> newPayment = new HashMap<>();
            newPayment.put("orderId", orderId);
            newPayment.put("amount", amount);
            newPayment.put("status", "COMPLETED");
            newPayment.put("timestamp", new Date());
            payments.put(orderId, newPayment);

            Map<String, Object> ret = new HashMap<>();
            ret.put("success", true);
            ret.put("transactionId", "TXN-" + new Date());
            ret.put("message", "결제가 승인되었습니다");

            return ret;
        } else {
            Map<String, Object> ret = new HashMap<>();
            ret.put("success", false);
            ret.put("reason", "카드 승인이 거부되었습니다");

            return ret;
        }
    }

    @Override
    public Map<String, Object> refundPayment(String orderId) {
        Object payment = payments.get(orderId);
        Map<String, Object> ret = new HashMap<>();

        if (!(payment instanceof Map)) {
            ret.put("status", "NOT_FOUND");
            return ret;
        }

        ret.put("status", payments.get("orderId"));
        ret.put("transactionId", payments.get("amount"));
        ret.put("message", payments.get("timestamp"));

        return ret;
    }

    @Override
    public String getPaymentStatus(String orderId) {
        return "";
    }
}
