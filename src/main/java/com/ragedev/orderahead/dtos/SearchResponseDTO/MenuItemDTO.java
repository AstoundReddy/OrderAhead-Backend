package com.ragedev.orderahead.dtos.SearchResponseDTO;


import lombok.Data;

import java.util.List;

@Data
public class MenuItemDTO {
    private Integer itemId;
    private String name;
    private String restaurantName;
    private Integer restaurantId;
    private Integer numberOfRatings;
    private Double averageRating;
}