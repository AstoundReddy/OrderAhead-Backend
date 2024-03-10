package com.ragedev.orderahead.controllers;
// OrderController.java

import com.fasterxml.jackson.annotation.JsonView;
import com.ragedev.orderahead.dtos.PlaceOrderDTO;
import com.ragedev.orderahead.dtos.UpdateOrderDTO;
import com.ragedev.orderahead.exceptions.ResourceNotFoundException;
import com.ragedev.orderahead.models.Order;
import com.ragedev.orderahead.models.OrderStatus;
import com.ragedev.orderahead.service.OrderService;
import com.ragedev.orderahead.view.Views;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleOrderNotFound(@NotNull ResourceNotFoundException ex) {
        return ex.getMessage();
    }

    @PostMapping("")
    public ResponseEntity<Integer> createOrder(@RequestBody PlaceOrderDTO orderRequest) {
        Order savedOrder = orderService.placeOrder(orderRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedOrder.getOrderId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @JsonView(Views.UserView.class)
    @GetMapping("/user/{id}")
    public List<Order> getOrdersByUser(@PathVariable Integer id) {
        return orderService.getOrdersByUser(id);
    }

    @JsonView(Views.RestaurantView.class)
    @GetMapping("/restaurant/{restaurantId}")
    public Page<Order> getOrdersByRestaurant(@PathVariable Integer restaurantId, @RequestParam(required = false) OrderStatus status, Pageable pageable) {
        return orderService.getOrdersByRestaurant(restaurantId, status, pageable);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<String> updateOrder(@PathVariable Integer orderId, @RequestBody UpdateOrderDTO request) {
        if (request.getPickupDateTime() != null) {
            orderService.updatePickupTime(orderId, request.getPickupDateTime());
        }
        if (request.getStatus() != null) {
            orderService.updateOrderStatus(orderId, request.getStatus());
        }
        return ResponseEntity.ok("Order updated successfully");
    }
    @PutMapping("/{orderId}/rating")
    public ResponseEntity<String> updateRating(@PathVariable Integer orderId, @RequestParam Integer rating){
        orderService.updateRating(orderId, rating);
        return ResponseEntity.ok("Rating updated successfully");
    }
}