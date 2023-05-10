package com.adega.ms.order.adegaorderservice.controllers;

import com.adega.ms.order.adegaorderservice.dtos.MailDto;
import com.adega.ms.order.adegaorderservice.dtos.OrderDto;
import com.adega.ms.order.adegaorderservice.enums.OrderStatus;
import com.adega.ms.order.adegaorderservice.models.OrderModel;
import com.adega.ms.order.adegaorderservice.services.MailService;
import com.adega.ms.order.adegaorderservice.services.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@Log4j2
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final LocalDateTime localDateTime;
    private final MailService mailService;
    private final OrderService orderService;

    @PostMapping("/create")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Object> createOrder(@RequestBody OrderDto orderDto) throws JsonProcessingException {
        if(!orderService.isStockValid(orderDto)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    "Some product does not has enough stock or may be unavailable :(");
        }
        orderService.discountStock(orderDto);
        var orderModel = new OrderModel();
        BeanUtils.copyProperties(orderDto, orderModel);
        orderModel.setStatus(OrderStatus.PLACED);
        orderModel.setRegistrationDate(localDateTime);
        orderModel.setLastUpdate(localDateTime);
        mailService.sendMailMessage(new MailDto(
                "gcaio7463@gmail.com",
                orderDto.getOwner(),
                "Thank You!",
                "Thanks for buying with us! See you next time ;)"));
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(orderModel));
    }

    @PutMapping("/edit")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Object> editOrderStatus(@RequestParam(value = "id") UUID id,
                                                  @RequestParam(value = "status") String status)
            throws JsonProcessingException {
        var optionalOrderModel = orderService.findById(id);
        if (optionalOrderModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found :(");
        }
        var orderModel = new OrderModel();
        BeanUtils.copyProperties(optionalOrderModel.get(), orderModel);
        orderModel.setStatus(OrderStatus.valueOf(status));
        mailService.sendMailMessage(new MailDto(
                "gcaio7463@gmail.com",
                orderModel.getOwner(),
                "Your order has been updated",
                String.format("Your order with id %s is now %s",
                        orderModel.getUuid(),
                        orderModel.getStatus().name())
        ));
        return ResponseEntity.ok(orderService.save(orderModel));
    }

    @GetMapping("/all")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Object> listAllOrders(@PageableDefault Pageable pageable) {
        var allOrders = orderService.findAll(pageable);
        if (allOrders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No order found :(");
        }
        return ResponseEntity.ok(allOrders);
    }

    @GetMapping("/one/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Object> listOrderById(@PathVariable(value = "id") UUID id) {
        var optionalOrderModel = orderService.findById(id);
        return optionalOrderModel.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(
                () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found :("));
    }

    @GetMapping("/user-orders")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Object> listOrderByUserEmail() {
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        var orderList = orderService.findAllByUserEmail(userEmail);
        if (orderList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No order found :(");
        }
        return ResponseEntity.ok(orderList);
    }

}
