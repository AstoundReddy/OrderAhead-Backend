package com.ragedev.orderahead.controllers;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.ragedev.orderahead.view.Views;
import org.springframework.beans.factory.annotation.Autowired;

import com.ragedev.orderahead.models.OrderDetails;
import com.ragedev.orderahead.service.OrderDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderDetails")
@CrossOrigin
public class OrderDetailsController {
    private final OrderDetailsService orderDetailsService;
    @Autowired
    public OrderDetailsController(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    @RequestMapping("/{orderId}")
    @JsonView(Views.OrderView.class)
    public List<OrderDetails> getOrderDetailsByOrderId(@PathVariable Integer orderId) {
        return orderDetailsService.getOrderDetailsByOrderId(orderId);
    }
    @PutMapping("/{orderDetailsId}")
    public ResponseEntity<String> updateItemRating(@PathVariable Integer orderDetailsId, @RequestParam Integer rating) {
        orderDetailsService.rateItem(orderDetailsId, rating);
        return ResponseEntity.ok("Item rated successfully");
    }
}
