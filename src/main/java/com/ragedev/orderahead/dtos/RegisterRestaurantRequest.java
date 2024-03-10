package com.ragedev.orderahead.dtos;

import com.ragedev.orderahead.models.Restaurant;
import com.ragedev.orderahead.models.User;
import lombok.Data;

@Data
public class RegisterRestaurantRequest {
    private User user;
    private Restaurant restaurant;
}
