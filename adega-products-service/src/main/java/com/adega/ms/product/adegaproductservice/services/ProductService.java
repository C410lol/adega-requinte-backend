package com.adega.ms.product.adegaproductservice.services;

import com.adega.ms.product.adegaproductservice.enums.ProductStatus;
import com.adega.ms.product.adegaproductservice.models.ProductModel;
import com.adega.ms.product.adegaproductservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProductService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ProductRepository productRepository;

    public void deleteProductById(UUID uuid) {
        productRepository.deleteById(uuid);
        log.info("Product has been deleted, Id -> {}", uuid);
    }

    public Page<ProductModel> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<ProductModel> findById(UUID uuid) {
        return productRepository.findById(uuid);
    }

    public ProductModel save(@NotNull ProductModel productModel) {
        productRepository.save(productModel);
        log.info("Product has been saved -> {}", productModel);
        kafkaTemplate.send("mails_products", productModel.getName());
        return productModel;
    }

    public void verifyProductStock(@NotNull ProductModel productModel) {
        if (productModel.getStock() <= 0) {
            productModel.setStatus(ProductStatus.UNAVAILABLE);
        } else {
            productModel.setStatus(ProductStatus.AVAILABLE);
        }
    }

}
