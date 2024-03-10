package com.ragedev.orderahead.repository;

import com.ragedev.orderahead.models.Order;
import com.ragedev.orderahead.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    List<OrderDetails> findByOrder_OrderId(Integer orderId);
    OrderDetails findByOrderDetailsIdAndMenuItem_ItemId(Integer orderDetailsId, Integer itemId);
}
