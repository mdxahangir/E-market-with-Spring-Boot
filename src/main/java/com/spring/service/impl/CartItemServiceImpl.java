package com.spring.service.impl;

import com.spring.model.CartItem;
import com.spring.model.Product;
import com.spring.repository.CartItemRepository;
import com.spring.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        Product product = cartItem.getProduct();

        BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());
        BigDecimal price = product.getPrice();

        // Subtotal
        BigDecimal subtotal = price.multiply(quantity);

        // Tax (example: 10% VAT)
        BigDecimal taxRate = new BigDecimal("0.10");
        BigDecimal tax = subtotal.multiply(taxRate);

        // Discount from product
        BigDecimal discountRate = BigDecimal.valueOf(product.getDiscount()).divide(BigDecimal.valueOf(100));
        BigDecimal discount = subtotal.multiply(discountRate);

        // Shipping fixed (or use from ShippingMethod)
        BigDecimal shipping = new BigDecimal("50.00");

        // Total = subtotal + tax + shipping - discount
        BigDecimal total = subtotal.add(tax).add(shipping).subtract(discount);

        // Set all calculated values
        cartItem.setSubtotal(subtotal);
        cartItem.setTax(tax);
        cartItem.setDiscount(discount);
        cartItem.setShipping(shipping);
        cartItem.setTotal(total);

        return cartItemRepository.save(cartItem);
    }

    @Override
    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    @Override
    public Optional<CartItem> getCartItemById(Long id) {
        return cartItemRepository.findById(id);
    }
}
