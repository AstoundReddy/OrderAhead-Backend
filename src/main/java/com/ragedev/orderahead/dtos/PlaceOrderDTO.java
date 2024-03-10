package com.ragedev.orderahead.dtos;

import lombok.*;
import org.springframework.data.util.Pair;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlaceOrderDTO {
    private Integer restaurantId;
    private Integer userId;
    private List<ItemAndQuantity> items;
    private String specialInstructions;
    private Integer totalPrice;
    private Integer totalItems;
}
