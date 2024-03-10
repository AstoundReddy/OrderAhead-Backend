package com.ragedev.orderahead.controllers;
// RestaurantController.java

import com.ragedev.orderahead.dtos.RestaurantSummaryDTO;
import com.ragedev.orderahead.dtos.SearchResponseDTO.SearchResponseDTO;
import com.ragedev.orderahead.exceptions.ResourceNotFoundException;
import com.ragedev.orderahead.models.MenuItem;
import com.ragedev.orderahead.models.Restaurant;
import com.ragedev.orderahead.service.RestaurantService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/restaurants")
@CrossOrigin
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleRestaurantNotFound(@NotNull ResourceNotFoundException ex){
        return ex.getMessage();
    }

    @GetMapping("")
    public Page<RestaurantSummaryDTO> getAllRestaurants(@PageableDefault(size = 9) Pageable pageable) {
        return restaurantService.getAllRestaurants(pageable);
    }
    @PostMapping("/manager/{managerId}")
    public ResponseEntity<Integer> createRestaurant(@RequestBody Restaurant restaurant, @PathVariable Integer managerId) {
        restaurant.setNumRatings(0);
        restaurant.setAverageRating(0.0);
        Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurant,managerId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedRestaurant.getRestaurantId()).toUri();
        return ResponseEntity.created(location).build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Integer id){
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }
    @GetMapping("/manager/{id}")
    public ResponseEntity<Restaurant> getRestaurantByManagerId(@PathVariable Integer id){
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurantById(@PathVariable Integer id){
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public Restaurant updateRestaurant(@PathVariable Integer id, @RequestBody Restaurant restaurantDetails) {
        return restaurantService.updateRestaurant(id, restaurantDetails);
    }
    @PostMapping("/{id}/menuItem")
    public MenuItem createMenuItemInRestaurant(@PathVariable Integer id, @RequestBody MenuItem menuItem){
        return restaurantService.addMenuItemToRestaurant(id,menuItem);
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponseDTO> searchQuery(@RequestParam String searchString){
        return ResponseEntity.ok(restaurantService.searchQuery(searchString));
    }
}