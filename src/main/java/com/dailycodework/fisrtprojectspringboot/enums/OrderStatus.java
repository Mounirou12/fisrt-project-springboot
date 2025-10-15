package com.dailycodework.fisrtprojectspringboot.enums;

public enum OrderStatus {
    PENDING,//The order has been placed but not yet processed.
    PROCESSING,//The order is being prepared.
    SHIPPED,//The order has been sent out for delivery.
    DELIVERED,//The order has arrived at its destination.
    CANCELLED//The order has been cancelled.
}
