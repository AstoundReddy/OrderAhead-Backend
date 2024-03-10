package com.ragedev.orderahead.repository;

import com.ragedev.orderahead.models.Order;
import com.ragedev.orderahead.models.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByUser_UserId(Integer userId, Sort sort);
    Page<Order> findByRestaurant_RestaurantIdAndOrderStatus(Integer restaurantId, OrderStatus status, Pageable pageable);
}
