package com.spring.service;

import com.spring.model.OrderSummary;

import java.util.List;
import java.util.Optional;

public interface OrderSummaryService {

    OrderSummary saveOrderSummary(OrderSummary orderSummary);

    List<OrderSummary> getAllOrderSummaries();

    Optional<OrderSummary> getOrderSummaryById(Long id);

    OrderSummary updateOrderSummary(Long id, OrderSummary updatedOrderSummary);

    void deleteOrderSummary(Long id);
}
