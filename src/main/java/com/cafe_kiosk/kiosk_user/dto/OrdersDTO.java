package com.cafe_kiosk.kiosk_user.dto;

import com.cafe_kiosk.kiosk_user.domain.OrderStatus;
import com.cafe_kiosk.kiosk_user.domain.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTO {

    private Long orderId;
    private String phone;
    private LocalDateTime orderTime;
    private Long totalAmount;
    private OrderStatus orderStatus;
    private Long usedPoint = 0L;
    private Long earnedPoint = 0L;
    private String orderMethod;
    private String paymentKey;


    // Entity → DTO
    public static OrdersDTO entityToDto(Orders orders) {
        return OrdersDTO.builder()
                .orderId(orders.getOrderId())
                .phone(orders.getPhone())
                .orderTime(orders.getOrderTime())
                .totalAmount(orders.getTotalAmount())
                .orderStatus(orders.getOrderStatus())
                .usedPoint(orders.getUsedPoint())
                .earnedPoint(orders.getEarnedPoint())
                .orderMethod(orders.getOrderMethod())
                .paymentKey(orders.getPaymentKey())
                .build();
    }

    // DTO → Entity
    public Orders dtoToEntity() {
        return Orders.builder()
                .orderId(orderId)
                .phone(phone)
                .orderTime(orderTime)
                .totalAmount(totalAmount)
                .orderStatus(orderStatus)
                .usedPoint(usedPoint)
                .earnedPoint(earnedPoint)
                .orderMethod(orderMethod)
                .paymentKey(paymentKey)
                .build();
    }
}
