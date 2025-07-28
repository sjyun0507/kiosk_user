package com.cafe_kiosk.kiosk_user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false)
    private LocalDateTime orderTime;

    @Column(nullable = false)
    private Long totalAmount;

    @Column(nullable = false, length = 50)
    private String status;

    private Long usedPoint = 0L;

    private Long earnedPoint = 0L;

    @Column(nullable = false, length = 20)
    private String orderMethod;
}
