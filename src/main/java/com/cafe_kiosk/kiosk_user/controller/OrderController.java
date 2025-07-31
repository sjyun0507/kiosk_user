package com.cafe_kiosk.kiosk_user.controller;

import com.cafe_kiosk.kiosk_user.domain.OrderStatus;
import com.cafe_kiosk.kiosk_user.domain.Orders;
import com.cafe_kiosk.kiosk_user.domain.User;
import com.cafe_kiosk.kiosk_user.dto.OrdersDTO;
import com.cafe_kiosk.kiosk_user.dto.UserDTO;
import com.cafe_kiosk.kiosk_user.repository.OrderRepository;
import com.cafe_kiosk.kiosk_user.repository.UserRepository;
import com.cafe_kiosk.kiosk_user.service.MainService;
import com.cafe_kiosk.kiosk_user.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final MainService mainService;


    @PostMapping("/order")
    public ResponseEntity<Map<String,Object>> createOrder(@RequestBody OrdersDTO ordersDTO){
        String orderId = UUID.randomUUID().toString();// toss 전용 주문번호 생성
        // 1) 전화번호로 유저 조회 또는 생성
        UserDTO userDTO = mainService.findOrCreateUserByPhone(ordersDTO.getPhone());

        Long totalAmount = ordersDTO.getTotalAmount();
        if (totalAmount == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "totalAmount is required"));
        }
        long earnedPoint = (long)(totalAmount * 0.05);
        ordersDTO.setTossOrderId(orderId);
//        Orders order = Orders.builder()
//                .tossOrderId(orderId) //toss 에서 받은 orderId를 tossOrderId 필드에 저장
//                .phone(ordersDTO.getPhone())
//                .orderTime(ordersDTO.getOrderTime())
//                .orderStatus(ordersDTO.getOrderStatus())
//                .usedPoint(ordersDTO.getUsedPoint())
//                .totalAmount(ordersDTO.getTotalAmount())
//                .orderMethod(ordersDTO.getOrderMethod())
//                .earnedPoint(ordersDTO.getEarnedPoint())
//                .build();

        userDTO.setPoints(userDTO.getPoints() + earnedPoint);
        mainService.userSave(userDTO);

        ordersDTO.setOrderStatus(OrderStatus.WAITING);
        mainService.orderSave(ordersDTO);

        Map<String,Object> response = new HashMap<>();
        response.put("orderId", orderId);


        System.out.println("DTO 값: " + ordersDTO);
        System.out.println("DTO 값: " + userDTO);
        System.out.println("earnedPoint 계산 값: " + (ordersDTO.getTotalAmount() * 0.05));
        System.out.println("orderStatus: " + ordersDTO.getOrderStatus());

        return ResponseEntity.ok(response);
    }
}
