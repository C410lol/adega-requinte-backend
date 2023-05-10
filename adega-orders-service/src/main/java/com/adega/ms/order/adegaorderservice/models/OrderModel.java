package com.adega.ms.order.adegaorderservice.models;

import com.adega.ms.order.adegaorderservice.enums.OrderStatus;
import com.adega.ms.order.adegaorderservice.enums.PaymentMethod;
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
@Table(name = "order_tb")
@ToString
public class OrderModel {

    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID uuid;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDateTime lastUpdate;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(nullable=false)
    private List<UUID> productIds;

    @Column(nullable = false)
    private List<String> productImages;

    @Column(nullable=false)
    private List<String> productNames;

    @Column(nullable = false)
    private List<Double> productPrices;

    @Column(nullable=false)
    private List<Integer> productQuantity;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private Double totalPrice;

    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderModel that = (OrderModel) o;
        return getUuid() != null && Objects.equals(getUuid(), that.getUuid());
    }

    @Override public int hashCode() {
        return getClass().hashCode();
    }

}
