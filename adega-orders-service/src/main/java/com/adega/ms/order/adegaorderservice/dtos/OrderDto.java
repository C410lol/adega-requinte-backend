package com.adega.ms.order.adegaorderservice.dtos;

import com.adega.ms.order.adegaorderservice.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderDto {

    @NotBlank
    private String address;

    @NotBlank
    private String owner;

    @NotBlank
    private PaymentMethod paymentMethod;

    @NotNull
    private List<UUID> productIds;

    @NotNull
    private List<String> productImages;

    @NotNull
    private List<String> productNames;

    @NotNull
    private List<Double> productPrices;

    @NotNull
    private List<Integer> productQuantity;

    @NotNull
    private Double totalPrice;

}
