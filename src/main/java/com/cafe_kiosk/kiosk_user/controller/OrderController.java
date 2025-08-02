package com.cafe_kiosk.kiosk_user.controller;

import com.cafe_kiosk.kiosk_user.domain.OrderStatus;
import com.cafe_kiosk.kiosk_user.dto.OrdersDTO;
import com.cafe_kiosk.kiosk_user.dto.UserDTO;
import com.cafe_kiosk.kiosk_user.repository.OrderRepository;
import com.cafe_kiosk.kiosk_user.service.MenuService;
import com.cafe_kiosk.kiosk_user.service.OrderService;
import com.cafe_kiosk.kiosk_user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class OrderController {
    private final UserService userService;
    private final OrderService orderService;


    @PostMapping("/")
    public ResponseEntity<Map<String,Object>> createOrder(@RequestBody OrdersDTO ordersDTO){
        String orderId = UUID.randomUUID().toString();// toss 전용 주문번호 생성
        // 1) 전화번호로 유저 조회 또는 생성
        UserDTO userDTO = userService.findOrCreateUserByPhone(ordersDTO.getPhone());

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

//        userDTO.setPoints(userDTO.getPoints() + earnedPoint);
        userService.userSave(userDTO);

        ordersDTO.setStatus(OrderStatus.WAITING);
        orderService.orderSave(ordersDTO);

        Map<String,Object> response = new HashMap<>();
        response.put("orderId", orderId);


        System.out.println("DTO 값: " + ordersDTO);
        System.out.println("DTO 값: " + userDTO);
        System.out.println("earnedPoint 계산 값: " + (ordersDTO.getTotalAmount() * 0.05));
        System.out.println("orderStatus: " + ordersDTO.getStatus());

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/user/points/{phone}")
//    public ResponseEntity<Integer> getUserPoint(@PathVariable String phone) {
//        UserDTO userDTO = mainService.getUser(phone);
//        Integer points = Math.toIntExact(userDTO.getPoints());
//        return ResponseEntity.ok(points);      }
    @GetMapping("/points")
    public ResponseEntity<Map<String, Object>> getUserPoints(@RequestParam String phone) {
        UserDTO userDTO = userService.findOrCreateUserByPhone(phone);

        if (userDTO != null) {
            return ResponseEntity.ok(Map.of("points", userDTO.getPoints()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("points", 0));
        }
    }

}
