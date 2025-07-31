package com.cafe_kiosk.kiosk_user.service;

import com.cafe_kiosk.kiosk_user.domain.OrderStatus;
import com.cafe_kiosk.kiosk_user.repository.OrderRepository;
import com.cafe_kiosk.kiosk_user.domain.Orders;
import com.cafe_kiosk.kiosk_user.dto.OrdersDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public String getOrderId(){
        // 주문 번호 생성
        String orderId= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss"))
                + "__" + UUID.randomUUID();
        log.info(orderId);
        return orderId;
    }
    public void addDummyOrder(Long orderId){
        Orders dummyOrder = Orders.builder()
                .phone("01034009644") // 토스에서 휴대폰 번호에 '-' 들어가면 에러남
                .totalAmount(10000L)
                .orderMethod("kiosk")
                .build();
        log.info(dummyOrder);
        orderRepository.save(dummyOrder);
    }
    public OrdersDTO getOrder(Long orderId){
        log.info("orderId : {}", orderId);
        Orders order = orderRepository.findByOrderId(orderId);
        log.info("order: {}", order);

        return modelMapper.map(order, OrdersDTO.class);
    }

    public void modifyOrderStatus(Long orderId, OrderStatus orderStatus){
        orderRepository.updateOrderStatus(orderId, orderStatus);
    }

    public void modifyPaymentKey(Long orderId, String paymentKey){
        orderRepository.updatePaymentKey(orderId, paymentKey);
    }

    public Long getOrderIdByTossOrderId(String tossOrderId) {
        Orders order = orderRepository.findByTossOrderId(tossOrderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 tossOrderId의 주문이 없습니다."));
        return order.getOrderId();
    } //DB에서 Orders 찾고 → orderId(Long)를 반환함
}
