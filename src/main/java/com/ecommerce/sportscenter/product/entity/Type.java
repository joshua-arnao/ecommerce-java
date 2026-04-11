package com.ecommerce.sportscenter.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "types")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private List<Product> products;
}
