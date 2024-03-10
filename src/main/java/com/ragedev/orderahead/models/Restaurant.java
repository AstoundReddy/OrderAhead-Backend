package com.ragedev.orderahead.models;

import com.fasterxml.jackson.annotation.*;
import com.ragedev.orderahead.view.Views;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restaurantId;

    @Column(nullable = false)
    @JsonView({Views.UserView.class, Views.RestaurantView.class})
    private String name;

    @JsonView({Views.UserView.class, Views.RestaurantView.class})
    private String location;

    @Column(columnDefinition = "TEXT")
    @JsonView({Views.RestaurantView.class})
    private String hoursOfOperation;

    private String cuisineType;

    @Column(nullable = false)
    private String phoneNumber;

    private Integer numRatings ;
    private Double averageRating ;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("rest-items")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<MenuItem> menuItems;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @JsonManagedReference("rest-order")
    @JsonIgnore
    private List<Order> orders;

    @OneToOne
    @JoinColumn(name = "manager_id")
    @JsonBackReference
    private User manager;

}
