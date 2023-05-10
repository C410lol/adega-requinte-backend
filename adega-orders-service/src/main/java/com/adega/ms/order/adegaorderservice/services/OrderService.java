package com.adega.ms.order.adegaorderservice.services;

import com.adega.ms.order.adegaorderservice.dtos.OrderDto;
import com.adega.ms.order.adegaorderservice.models.OrderModel;
import com.adega.ms.order.adegaorderservice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public void discountStock(@NotNull OrderDto orderDto) {
        for(int i = 0; i < orderDto.getProductIds().size(); i++) {
            restTemplate.put(
                    "http://ADEGA-PRODUCTS-SERVICE/products/discount-stock?id="
                            + orderDto.getProductIds().get(i)
                            + "&quantity="
                            + orderDto.getProductQuantity().get(i),
                    null);
        }
    }

    public Page<OrderModel> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public List<OrderModel> findAllByUserEmail(String email) {
        List<OrderModel> ordersList = new ArrayList<>();
        for (OrderModel orderModel: orderRepository.findAll()) {
            if (orderModel.getOwner().equals(email)) {
                ordersList.add(orderModel);
            }
        }
        return ordersList;
    }

    public Optional<OrderModel> findById(UUID id) {
        return orderRepository.findById(id);
    }

    public boolean isStockValid(@NotNull OrderDto orderDto) {
        for(int i = 0; i < orderDto.getProductIds().size(); i++){
            var productStock = Objects.requireNonNull(restTemplate.getForObject(
                    "http://ADEGA-PRODUCTS-SERVICE/products/verify-product?id="
                            + orderDto.getProductIds().get(i),
                    Integer.class));
            if(orderDto.getProductQuantity().get(i) > productStock) {
                return false;
            }
        }
        return true;
    }

    public OrderModel save(OrderModel orderModel) {
        orderRepository.save(orderModel);
        log.info("Order has been saved -> {}", orderModel);
        return orderModel;
    }

}
