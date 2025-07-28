package com.cafe_kiosk.kiosk_user.repository;

import com.cafe_kiosk.kiosk_user.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByPhone(String phone);
}