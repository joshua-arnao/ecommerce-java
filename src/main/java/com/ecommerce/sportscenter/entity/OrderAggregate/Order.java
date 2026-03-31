package com.ecommerce.sportscenter.entity.OrderAggregate;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "shoppingcart_id")
    private String shoppingCartId;

    @Embedded
    private ShippingAddress shippingAddress;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderItem> orderItems;

    @Column(name = "sub_total")
    private BigDecimal subTotal;

    @Column(name = "delivery_fee")
    private BigDecimal deliveryFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column(name = "order_date", updatable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
    }

    public BigDecimal getTotal() {
        return getSubTotal().add(getDeliveryFee());
    }

}
