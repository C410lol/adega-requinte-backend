package com.adega.ms.order.adegaorderservice.repositories;

import com.adega.ms.order.adegaorderservice.models.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, UUID> {
}
