package com.ragedev.orderahead.service;

import com.ragedev.orderahead.dtos.RestaurantSummaryDTO;
import com.ragedev.orderahead.dtos.SearchResponseDTO.MenuItemDTO;
import com.ragedev.orderahead.dtos.SearchResponseDTO.RestaurantDTO;
import com.ragedev.orderahead.dtos.SearchResponseDTO.SearchResponseDTO;
import com.ragedev.orderahead.exceptions.ResourceNotFoundException;
import com.ragedev.orderahead.models.MenuItem;
import com.ragedev.orderahead.models.Restaurant;
import com.ragedev.orderahead.models.User;
import com.ragedev.orderahead.repository.MenuItemRepository;
import com.ragedev.orderahead.repository.RestaurantRepository;
import com.ragedev.orderahead.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
    }
    public Page<RestaurantSummaryDTO> getAllRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(pageable).map(this::mapToRestaurantSummaryDTO);
    }

    private RestaurantSummaryDTO mapToRestaurantSummaryDTO(Restaurant restaurant) {
        RestaurantSummaryDTO dto = new RestaurantSummaryDTO();
        dto.setRestaurantId(restaurant.getRestaurantId());
        dto.setName(restaurant.getName());
        dto.setCuisineType(restaurant.getCuisineType());
        dto.setAverageRating(restaurant.getAverageRating());
        dto.setNumRatings(restaurant.getNumRatings());
        dto.setHoursOfOperation(restaurant.getHoursOfOperation());
        return dto;
    }
    public Restaurant saveRestaurant(Restaurant restaurant, Integer managerId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + managerId));
        if(restaurant.getNumRatings() == null) restaurant.setNumRatings(0);
        if(restaurant.getAverageRating() == null) restaurant.setAverageRating(0.0);
        restaurant.setManager(manager);
        return restaurantRepository.save(restaurant);
    }
    public Restaurant getRestaurantById(Integer id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + id));;
        return restaurant;
    }

    public Restaurant getRestaurantByManagerId(Integer id) {
        Restaurant restaurant = restaurantRepository.findByManager_UserId(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found for manager with id " + id));;
        return restaurant;
    }
    public void deleteRestaurant(Integer id) {
        if (!restaurantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurant not found with id " + id);
        }
        restaurantRepository.deleteById(id);
    }
    // RestaurantService.java

    public Restaurant updateRestaurant(Integer id, Restaurant restaurantDetails) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + id));

        restaurant.setName(restaurantDetails.getName());
        restaurant.setLocation(restaurantDetails.getLocation());
        restaurant.setCuisineType(restaurantDetails.getCuisineType());
        restaurant.setPhoneNumber(restaurantDetails.getPhoneNumber());
        restaurant.setHoursOfOperation(restaurantDetails.getHoursOfOperation());
        restaurant.setAverageRating(0.0);
        restaurant.setNumRatings(0);
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public MenuItem addMenuItemToRestaurant(Integer restaurantId, MenuItem menuItem) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        menuItem.setRestaurant(restaurant);
        menuItem.setAverageRating((0.0));
        menuItem.setNumberOfRatings(0);
        return menuItemRepository.save(menuItem);
    }

    public SearchResponseDTO searchQuery(String query){

        List <Restaurant> restaurants = restaurantRepository.findRestaurantsByQuery(query);
        List <MenuItem> menuItems = menuItemRepository.findByQuery(query);

        List<MenuItemDTO> menuItemDTOs = menuItems.stream().map(item -> {
            MenuItemDTO dto = new MenuItemDTO();
            dto.setItemId(item.getItemId());
            dto.setName(item.getName());
            dto.setRestaurantName(item.getRestaurant().getName());
            dto.setRestaurantId(item.getRestaurant().getRestaurantId());
            dto.setNumberOfRatings(item.getNumberOfRatings());
            dto.setAverageRating(item.getAverageRating());
            return dto;
        }).collect(Collectors.toList());

        List<RestaurantDTO> restaurantDTOs = restaurants.stream().map(restaurant -> {
            RestaurantDTO dto = new RestaurantDTO();
            dto.setRestaurantId(restaurant.getRestaurantId());
            dto.setName(restaurant.getName());
            dto.setNumberOfRatings(restaurant.getNumRatings());
            dto.setAverageRating(restaurant.getAverageRating());
            return dto;
        }).collect(Collectors.toList());

        SearchResponseDTO response = new SearchResponseDTO();
        response.setMenuItems(menuItemDTOs);
        response.setRestaurants(restaurantDTOs);
        return response;
    }
}

