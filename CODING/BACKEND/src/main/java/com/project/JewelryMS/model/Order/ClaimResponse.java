package com.project.JewelryMS.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class ClaimResponse {
    private boolean success;
    private Long orderId;
    private String userId;
    private String message;

    // Constructors
    public ClaimResponse() {}

    public ClaimResponse(boolean success, Long orderId, String userId) {
        this.success = success;
        this.orderId = orderId;
        this.userId = userId;
    }

    public ClaimResponse(boolean success, Long orderId, String userId, String message) {
        this.success = success;
        this.orderId = orderId;
        this.userId = userId;
        this.message = message;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}