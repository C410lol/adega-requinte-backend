package com.adega.ms.product.adegaproductservice.models;

import com.adega.ms.product.adegaproductservice.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Table(name = "product_tb")
@ToString
public class ProductModel {

    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID uuid;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    private List<String> images;

    @Column(nullable = false)
    private LocalDateTime lastUpdate;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProductStatus status;

    @Column(nullable = false)
    private Integer stock;

    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductModel that = (ProductModel) o;
        return getUuid() != null && Objects.equals(getUuid(), that.getUuid());
    }

    @Override public int hashCode() {
        return getClass().hashCode();
    }

}
