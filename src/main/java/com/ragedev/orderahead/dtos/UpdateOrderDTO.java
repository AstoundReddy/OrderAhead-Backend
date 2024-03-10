package com.ragedev.orderahead.dtos;

import com.ragedev.orderahead.models.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateOrderDTO {
    public LocalDateTime pickupDateTime;
    public OrderStatus status;
}
