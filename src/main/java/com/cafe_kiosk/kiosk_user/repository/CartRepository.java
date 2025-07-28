package com.cafe_kiosk.kiosk_user.repository;

import com.cafe_kiosk.kiosk_user.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByPhone(String phone);
    void deleteByPhone(String phone);
    Optional<Cart> findByPhoneAndMenu_MenuIdAndOption_OptionId(String phone, Long menuId, Long optionId);
}