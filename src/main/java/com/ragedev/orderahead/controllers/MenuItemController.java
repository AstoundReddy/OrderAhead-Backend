package com.ragedev.orderahead.controllers;
// MenuItemController.java

import com.ragedev.orderahead.exceptions.ResourceNotFoundException;
import com.ragedev.orderahead.models.MenuItem;
import com.ragedev.orderahead.service.MenuItemService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin
public class MenuItemController {
    private final MenuItemService menuItemService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleMenuItemNotFound(@NotNull ResourceNotFoundException ex){
        return ex.getMessage();
    }

    @GetMapping("/menuItems")
    public List<MenuItem> getAllMenuItems() {
        return menuItemService.getAllMenuItems();
    }
    @PostMapping("/menuItems")
    public ResponseEntity<Integer> createMenuItem(@RequestBody MenuItem menuItem) {
        MenuItem savedMenuItem = menuItemService.saveMenuItem(menuItem);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedMenuItem.getItemId()).toUri();
        return ResponseEntity.created(location).build();
    }
    @GetMapping("/menuItems/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Integer id){
        MenuItem menuItem = menuItemService.getMenuItemById(id);
        return ResponseEntity.ok(menuItem);
    }
    @DeleteMapping("/menuItems/{id}")
    public ResponseEntity<Void> deleteMenuItemById(@PathVariable Integer id){
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/menuItems/{id}")
    public MenuItem updateMenuItem(@PathVariable Integer id, @RequestBody MenuItem menuItemDetails) {
        return menuItemService.updateMenuItem(id, menuItemDetails);
    }
    @PutMapping("/menuItems/{menuItemId}/availability")
    public void updateMenuItemAvailability(@PathVariable Integer menuItemId, @RequestBody Boolean availability) {
        menuItemService.updateMenuItemAvailability(menuItemId, availability);
    }
}