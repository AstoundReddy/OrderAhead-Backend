package com.ragedev.orderahead.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "order_details")
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.OrderView.class)
    private Integer orderDetailsId;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @JsonView(Views.OrderView.class)
    private MenuItem menuItem;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference("order-ordDetails")
    private Order order;
    @JsonView(Views.OrderView.class)
    private Integer quantity;

    @JsonView(Views.OrderView.class)
    private Integer rating;

}
