package com.spring.service;

import com.spring.model.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemService {
    CartItem createCartItem(CartItem cartItem);
    List<CartItem> getAllCartItems();
    Optional<CartItem> getCartItemById(Long id);
}
