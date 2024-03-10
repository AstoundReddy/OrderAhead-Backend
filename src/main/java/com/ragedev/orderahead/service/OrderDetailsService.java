package com.ragedev.orderahead.service;

import java.util.List;

import com.ragedev.orderahead.exceptions.ResourceNotFoundException;
import com.ragedev.orderahead.models.MenuItem;
import com.ragedev.orderahead.models.OrderDetails;
import com.ragedev.orderahead.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ragedev.orderahead.repository.OrderDetailsRepository;

@Service
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public OrderDetailsService(OrderDetailsRepository orderDetailsRepository, MenuItemRepository menuItemRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public List<OrderDetails> getOrderDetailsByOrderId(Integer orderId) {
        return orderDetailsRepository.findByOrder_OrderId(orderId);
    }
    public void rateItem(Integer orderDetailsId, Integer rating) {
        // Find the order details by orderDetailsId and itemId
        OrderDetails orderDetails = orderDetailsRepository.findById(orderDetailsId).orElseThrow(()-> new ResourceNotFoundException("Item found with id " + orderDetailsId));
        MenuItem menuItem = menuItemRepository.findById(orderDetails.getMenuItem().getItemId()).orElseThrow(() -> new ResourceNotFoundException("Item found with id " +orderDetails.getMenuItem().getItemId() ));
        menuItem.setNumberOfRatings(menuItem.getNumberOfRatings() + 1);
        menuItem.setAverageRating((menuItem.getAverageRating() * (menuItem.getNumberOfRatings() - 1) + rating) / menuItem.getNumberOfRatings());
        orderDetails.setRating(rating);

        // Save the order details
        orderDetailsRepository.save(orderDetails);
    }


}
