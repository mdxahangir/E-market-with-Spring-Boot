package com.spring.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User ID (can be replaced with @ManyToOne relation with User entity in the future)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Total price of the cart (calculated from items)
    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    // Total quantity of items in the cart
    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    // Status of the cart: ACTIVE, ORDERED, CANCELLED, etc.
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private CartStatus status;

    // A cart can have multiple cart items
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItem> cartItems;
}
