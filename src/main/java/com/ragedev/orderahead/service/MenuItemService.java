package com.ragedev.orderahead.service;

import com.ragedev.orderahead.exceptions.ResourceNotFoundException;
import com.ragedev.orderahead.models.MenuItem;
import com.ragedev.orderahead.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }
    public MenuItem saveMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }
    public MenuItem getMenuItemById(Integer id) {
        return menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + id));
    }

    public void deleteMenuItem(Integer id) {
        if (!menuItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("MenuItem not found with id " + id);
        }
        menuItemRepository.deleteById(id);
    }
    public MenuItem updateMenuItem(Integer id, MenuItem menuItemDetails) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + id));
        menuItem.setName(menuItemDetails.getName());
        menuItem.setDescription(menuItemDetails.getDescription());
        menuItem.setImage(menuItemDetails.getImage());
        menuItem.setPrice(menuItemDetails.getPrice());
        menuItem.setAvailability(menuItemDetails.getAvailability());
        return menuItemRepository.save(menuItem);
    }

    public void updateMenuItemAvailability(Integer menuItemId, Boolean availability) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found" + menuItemId));

        menuItem.setAvailability(availability);

        menuItemRepository.save(menuItem);
    }
}

