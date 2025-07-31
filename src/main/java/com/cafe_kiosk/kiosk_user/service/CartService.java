package com.cafe_kiosk.kiosk_user.service;

import com.cafe_kiosk.kiosk_user.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    public void clearCartBySessionId(String sessionId) {
        cartRepository.deleteBySessionId(sessionId);
    }
}
