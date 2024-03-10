package com.ragedev.orderahead.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.ragedev.orderahead.view.Views;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.UserView.class, Views.RestaurantView.class})
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
//    @JsonBackReference("order-rest")
    @JsonView(Views.UserView.class)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name ="user_id")
//    @JsonBackReference("order-user")
    @JsonView(Views.RestaurantView.class)
    private User user;

    @JsonView({Views.UserView.class, Views.RestaurantView.class})
    private LocalDateTime orderDatetime;

    @JsonView({Views.UserView.class, Views.RestaurantView.class})
    private LocalDateTime pickupDatetime;

    @JsonView({Views.UserView.class, Views.RestaurantView.class})
    private String specialInstructions;

    @JsonView({Views.UserView.class, Views.RestaurantView.class})
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @JsonView({Views.UserView.class, Views.RestaurantView.class})
    private Integer totalPrice;

    @JsonView({Views.UserView.class, Views.RestaurantView.class})
    private Integer totalItems;
    @JsonView({Views.UserView.class, Views.RestaurantView.class})
    private Integer rating;
}
