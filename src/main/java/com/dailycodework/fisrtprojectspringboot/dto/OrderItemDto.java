package com.dailycodework.fisrtprojectspringboot.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemDto {

    private Long productId;
    private String productName;
    private String productBrand;// Add this field 
    private int quantity;
    private BigDecimal price;

}
