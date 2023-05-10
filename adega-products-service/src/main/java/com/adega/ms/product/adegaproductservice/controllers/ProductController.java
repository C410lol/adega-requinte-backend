package com.adega.ms.product.adegaproductservice.controllers;

import com.adega.ms.product.adegaproductservice.dtos.ProductDto;
import com.adega.ms.product.adegaproductservice.enums.ProductStatus;
import com.adega.ms.product.adegaproductservice.models.ProductModel;
import com.adega.ms.product.adegaproductservice.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final LocalDateTime localDateTime;

    @PostMapping("/create")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<ProductModel> createProduct(@RequestBody @Valid ProductDto productDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productDto, productModel);
        productService.verifyProductStock(productModel);
        productModel.setRegistrationDate(localDateTime);
        productModel.setLastUpdate(localDateTime);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productModel));
    }

    @DeleteMapping("/delete/{uuid}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "uuid") UUID uuid) {
        Optional<ProductModel> optionalProductModel = productService.findById(uuid);
        if (optionalProductModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found :(");
        }
        productService.deleteProductById(uuid);
        return ResponseEntity.ok("Product deleted successfully!");
    }

    @PutMapping("/discount-stock")
    @ResponseBody
    public Object discountStock(@RequestParam(value = "uuid") UUID uuid,
                                @RequestParam(value = "quantity") Integer quantity) {
        var optionalProductModel = productService.findById(uuid);
        if(optionalProductModel.isPresent()) {
            var productModel = new ProductModel();
            BeanUtils.copyProperties(optionalProductModel.get(), productModel);
            var finalStock = optionalProductModel.get().getStock() - quantity;
            if(finalStock <= 0) {
                productModel.setStatus(ProductStatus.UNAVAILABLE);
            }
            productModel.setStock(finalStock);
            productModel.setLastUpdate(localDateTime);
            productService.save(productModel);
        }
        return null;
    }

    @PutMapping("/edit/{uuid}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Object> editProduct(@PathVariable(value = "uuid") UUID uuid,
                                              @RequestBody @Valid ProductDto productDto) {
        Optional<ProductModel> optionalProductModel = productService.findById(uuid);
        if (optionalProductModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found :(");
        }
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productDto, productModel);
        productService.verifyProductStock(productModel);
        productModel.setRegistrationDate(optionalProductModel.get().getRegistrationDate());
        productModel.setLastUpdate(localDateTime);
        return ResponseEntity.ok(productService.save(productModel));
    }

    @GetMapping("/all")
    public ResponseEntity<Object> listAllProducts(@PageableDefault Pageable pageable) {
        Page<ProductModel> allProducts = productService.findAll(pageable);
        if (allProducts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No product found :(");
        }
        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/one/{uuid}")
    public ResponseEntity<Object> listProductById(@PathVariable(value = "uuid") UUID uuid) {
        Optional<ProductModel> optionalProductModel = productService.findById(uuid);
        return optionalProductModel.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(
                () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found :("));
    }

    @GetMapping("/verify-product")
    @ResponseBody
    public Integer verifyProduct(@RequestParam(value = "uuid") UUID uuid) {
        var optionalProductModel = productService.findById(uuid);
        if(optionalProductModel.isPresent()
                && optionalProductModel.get().getStatus().equals(ProductStatus.AVAILABLE)) {
            return optionalProductModel.get().getStock();
        }
        return 0;
    }

}
