package com.ragedev.orderahead.repository;

import com.ragedev.orderahead.models.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem,Integer> {
    @Query("SELECT m FROM MenuItem m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%',?1,'%'))")
    List<MenuItem> findByQuery(String name);
}