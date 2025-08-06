package com.spring.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private String userCode;
    private Long productId;
    private int quantity;
}
