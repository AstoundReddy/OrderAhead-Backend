package com.ragedev.orderahead.service;

import com.ragedev.orderahead.dtos.ItemAndQuantity;
import com.ragedev.orderahead.dtos.PlaceOrderDTO;
import com.ragedev.orderahead.exceptions.ResourceNotFoundException;
import com.ragedev.orderahead.models.*;
import com.ragedev.orderahead.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public OrderService(UserRepository userRepository, RestaurantRepository restaurantRepository,
            OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository,
            MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional
    public Order placeOrder(PlaceOrderDTO orderRequest) {
        // Create and save the Order
        Restaurant restaurant = restaurantRepository.findById(orderRequest.getRestaurantId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Restaurant not found" + orderRequest.getRestaurantId()));

        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found" + orderRequest.getUserId()));
        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setOrderStatus(OrderStatus.PLACED);
        order.setOrderDatetime(LocalDateTime.now());
        order.setSpecialInstructions(orderRequest.getSpecialInstructions());
        order.setTotalPrice(orderRequest.getTotalPrice());
        order.setTotalItems(orderRequest.getTotalItems());
        order = orderRepository.save(order);

        // Create and save the OrderDetails
        for (ItemAndQuantity orderItem : orderRequest.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(orderItem.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found" + orderItem.getItemId()));
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(order);
            orderDetails.setMenuItem(menuItem);
            orderDetails.setQuantity(orderItem.getQuantity());
            orderDetailsRepository.save(orderDetails);
        }
        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUser(Integer id) {
        Sort sort = Sort.by(Sort.Direction.DESC, "orderId");
        return orderRepository.findByUser_UserId(id, sort);
    }
    public Page<Order> getOrdersByRestaurant(Integer restaurantId, OrderStatus status, Pageable pageable) {
        return orderRepository.findByRestaurant_RestaurantIdAndOrderStatus(restaurantId, status, pageable);
    }

    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found" + id));
    }

    public void updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found" + orderId));
        order.setOrderStatus(newStatus);
        orderRepository.save(order);
    }


    public void updatePickupTime(Integer orderId, LocalDateTime pickupTime) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found" + orderId));
        order.setPickupDatetime(pickupTime);
        orderRepository.save(order);
    }

    public void updateRating(Integer orderId,Integer rating){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found" + orderId));
        order.setRating(rating);
        Restaurant restaurant = order.getRestaurant();

        // Update the number of ratings
        restaurant.setNumRatings(restaurant.getNumRatings() + 1);

        double totalRating = restaurant.getAverageRating() * (restaurant.getNumRatings() - 1) + rating;
        restaurant.setAverageRating(totalRating / restaurant.getNumRatings());

        restaurantRepository.save(restaurant);
        orderRepository.save(order);
    }
}
