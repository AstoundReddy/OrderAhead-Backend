package com.ragedev.orderahead.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.ragedev.orderahead.view.Views;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;
    @Column(nullable = false)
    @JsonView(Views.OrderView.class)
    private String name;
    private String description;
    @Column(nullable = false)
    @JsonView(Views.OrderView.class)
    private Double price;
    @JsonView(Views.OrderView.class)
    private String image;
    private Boolean availability;
    private Double averageRating;
    private Integer numberOfRatings;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
