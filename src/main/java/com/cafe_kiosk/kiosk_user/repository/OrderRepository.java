package com.cafe_kiosk.kiosk_user.repository;

import com.cafe_kiosk.kiosk_user.domain.OrderStatus;
import com.cafe_kiosk.kiosk_user.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
//    void insert(Orders orders);
//    Orders selectByOrderId(String orderId);
//    토스의 주문가 저장
    void updatePaymentKey(Long orderId, String paymentKey);
//    주문 상태 변경
    void updateOrderStatus(Long orderId, OrderStatus orderStatus);
    Orders findByOrderId(Long orderId);
}