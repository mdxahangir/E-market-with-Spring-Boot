package com.spring.service;

import com.spring.dto.AddToCartRequest;
import com.spring.dto.CartItemResponse;
import com.spring.dto.CartSummaryResponse;
import com.spring.model.Cart;
import com.spring.model.CartItem;
import com.spring.model.Product;
import com.spring.repository.CartItemRepository;
import com.spring.repository.CartRepository;
import com.spring.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public void addToCart(AddToCartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUserCode(request.getUserCode())
                .orElseGet(() -> Cart.builder()
                        .userCode(request.getUserCode())
                        .items(new ArrayList<>())
                        .build());

        CartItem item = CartItem.builder()
                .product(product)
                .quantity(request.getQuantity())
                .price(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                .cart(cart)
                .build();

        cart.getItems().add(item);
        cartRepository.save(cart); 
    }

    public CartSummaryResponse getCartSummary(String userCode) {
        Cart cart = cartRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> CartItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();

        int totalItems = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        BigDecimal totalPrice = cart.getItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartSummaryResponse.builder()
                .userCode(userCode)
                .totalItems(totalItems)
                .totalPrice(totalPrice)
                .items(itemResponses)
                .build();
    }
}
