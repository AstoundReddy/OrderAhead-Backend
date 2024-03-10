package com.ragedev.orderahead.dtos.SearchResponseDTO;

import lombok.Data;

import java.util.List;

@Data
public class RestaurantDTO {
    private Integer restaurantId;
    private String name;
    private Integer numberOfRatings;
    private Double averageRating;
}