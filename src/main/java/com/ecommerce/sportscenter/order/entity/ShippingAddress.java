package com.ecommerce.sportscenter.order.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ShippingAddress {
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zipcode;
    private String country;
}
