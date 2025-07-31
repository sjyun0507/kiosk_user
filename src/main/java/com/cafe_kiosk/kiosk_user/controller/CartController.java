package com.cafe_kiosk.kiosk_user.controller;

import com.cafe_kiosk.kiosk_user.dto.AddCartRequest;
import com.cafe_kiosk.kiosk_user.dto.CartDTO;
import com.cafe_kiosk.kiosk_user.repository.CartRepository;
import com.cafe_kiosk.kiosk_user.service.MainService;
import com.cafe_kiosk.kiosk_user.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {
    private final MainService mainService;
    private final CartRepository cartRepository;

    @Operation(summary = "장바구니담기")
    @PostMapping(value="/")
    public ResponseEntity<CartDTO> addCart(@RequestBody AddCartRequest cart) {
        log.info(cart);
        log.info("Session ID: {}", cart.getSessionId());
        CartDTO result = mainService.addToCart(cart); // 결과 받아오기
        return ResponseEntity.ok(result);

    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<CartDTO> deleteCart(@PathVariable String sessionId) {
        cartRepository.deleteBySessionId(sessionId);
        return ResponseEntity.ok(new CartDTO());
    }
}
