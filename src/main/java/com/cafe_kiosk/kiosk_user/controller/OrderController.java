package com.cafe_kiosk.kiosk_user.controller;

import com.cafe_kiosk.kiosk_user.domain.OrderStatus;
import com.cafe_kiosk.kiosk_user.domain.Orders;
import com.cafe_kiosk.kiosk_user.dto.OrdersDTO;
import com.cafe_kiosk.kiosk_user.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {
    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @PostMapping("/order")
    public ResponseEntity<Map<String,Object>> createOrder(@RequestBody OrdersDTO ordersDTO){
        String orderId = UUID.randomUUID().toString();// toss 전용 주문번호 생성

        Long totalAmount = ordersDTO.getTotalAmount();
        if (totalAmount == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "totalAmount is required"));
        }
        long earnedPoint = (long)(totalAmount * 0.05);

        Orders order = Orders.builder()
                .tossOrderId(orderId) //toss 에서 받은 orderId를 tossOrderId 필드에 저장
                .phone(ordersDTO.getPhone())
                .orderTime(ordersDTO.getOrderTime())
                .totalAmount(totalAmount)
                .orderStatus(ordersDTO.getOrderStatus())
                .usedPoint(ordersDTO.getUsedPoint())
                .totalAmount(totalAmount)
                .orderMethod(ordersDTO.getOrderMethod())
                .build();

        ordersDTO.setOrderStatus(OrderStatus.WAITING);
        orderRepository.save(order);

        Map<String,Object> response = new HashMap<>();
        response.put("orderId", orderId);


        System.out.println("DTO 값: " + ordersDTO);
        System.out.println("earnedPoint 계산 값: " + (ordersDTO.getTotalAmount() * 0.05));
        System.out.println("orderStatus: " + ordersDTO.getOrderStatus());

        return ResponseEntity.ok(response);
    }
}
