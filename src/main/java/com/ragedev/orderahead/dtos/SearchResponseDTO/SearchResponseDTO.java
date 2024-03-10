package com.ragedev.orderahead.dtos.SearchResponseDTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class SearchResponseDTO {
    private List<MenuItemDTO> menuItems;
    private List<RestaurantDTO> restaurants;
}
