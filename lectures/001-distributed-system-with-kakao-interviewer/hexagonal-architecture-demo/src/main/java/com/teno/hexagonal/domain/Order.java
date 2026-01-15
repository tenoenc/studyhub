package com.teno.hexagonal.domain;

import lombok.Getter;

import java.util.Date;

@Getter
public class Order {
    private String id;
    private String userId;
    private String productId;
    private int amount;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String failureReason;

    public Order(String id, String userId, String productId, int amount) {
        this(id, userId, productId, amount, "PENDING");
    }

    public Order(String id, String userId, String productId, int amount, String status) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
        this.status = status != null ? status : "PENDING";
        this.failureReason = "";
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public void markAsPaid() {
        if (!this.status.equals("PENDING")) {
            throw new RuntimeException("대기 중인 주문만 결제할 수 있습니다");
        }
        this.status = "PAID";
        this.updatedAt = new Date();
    }

    public void markAsFailed(String reason) {
        if (!this.status.equals("PENDING")) {
            throw new RuntimeException("대기 중인 주문만 실패 처리할 수 있습니다");
        }
        this.status = "FAILED";
        this.failureReason = reason;
        this.updatedAt = new Date();
    }

    public void cancel() {
        if (this.status.equals("PAID")) {
            throw new RuntimeException("이미 결제된 주문은 취소할 수 없습니다.");
        }
        this.status = "CANCELED";
        this.updatedAt = new Date();
    }

    public boolean canBePaid() {
        return this.status.equals("PENDING") && this.amount > 0;
    }

    public boolean canBeCancelled() {
        return !this.status.equals("PAID");
    }
}