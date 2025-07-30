package com.cafe_kiosk.kiosk_user.repository;

import com.cafe_kiosk.kiosk_user.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMenu_MenuIdAndOption_OptionId(Long menuId, Long optionId);
    List<Cart> findBySessionId(String sessionId);
//    void deleteBySessionId(String sessionId);
    List<Cart> findAll();
}