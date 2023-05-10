package com.adega.ms.product.adegaproductservice.repositories;

import com.adega.ms.product.adegaproductservice.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
}
