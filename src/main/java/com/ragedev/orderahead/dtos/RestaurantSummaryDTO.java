package com.ragedev.orderahead.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantSummaryDTO {
    private Integer restaurantId;
    private String name;
    private String cuisineType;
    private Double averageRating;
    private Integer numRatings;
    private String hoursOfOperation;
}
