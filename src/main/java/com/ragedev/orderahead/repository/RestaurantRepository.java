package com.ragedev.orderahead.repository;

import com.ragedev.orderahead.models.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Integer> {
    Page<Restaurant> findAll(Pageable pageable);
    Optional<Restaurant> findByManager_UserId(Integer managerId);
    @Query(value = "SELECT * FROM restaurants m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%',?1,'%'))", nativeQuery = true)
    List<Restaurant> findRestaurantsByQuery(String query);
}
