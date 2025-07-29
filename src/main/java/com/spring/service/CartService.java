package com.spring.service;

import com.spring.model.Cart;

import java.util.List;

public interface CartService {
    Cart saveCart(Cart cart);
    Cart getCartById(Long id);
    List<Cart> getCartsByUserId(Long userId);
    void deleteCart(Long id);
}
