package com.cafe_kiosk.kiosk_user.controller;

import com.cafe_kiosk.kiosk_user.domain.Cart;
import com.cafe_kiosk.kiosk_user.domain.OrderItem;
import com.cafe_kiosk.kiosk_user.dto.AddCartRequest;
import com.cafe_kiosk.kiosk_user.dto.CartDTO;
import com.cafe_kiosk.kiosk_user.dto.MenuDTO;
import com.cafe_kiosk.kiosk_user.dto.OrderItemDTO;
import com.cafe_kiosk.kiosk_user.repository.CartRepository;
import com.cafe_kiosk.kiosk_user.repository.MenuRepository;
import com.cafe_kiosk.kiosk_user.repository.OrderItemRepository;
import com.cafe_kiosk.kiosk_user.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {
    private final CartService cartService;
    private final OrderItemRepository orderItemRepository;
    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;

    @Operation(summary = "장바구니담기")
    @PostMapping(value="/add")
    public ResponseEntity<CartDTO> addCart(@RequestBody AddCartRequest cart) {
        log.info(cart);
        log.info("Session ID: {}", cart.getSessionId());
        CartDTO result = cartService.addToCart(cart); // 결과 받아오기

        return ResponseEntity.ok(result);

    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<CartDTO> deleteCart(@PathVariable String sessionId) {
        cartService.clearCartBySessionId(sessionId);
        return ResponseEntity.ok(new CartDTO());
    }

    @DeleteMapping("/remove/{ItemId}")
    public ResponseEntity<Void> deleteCartOne(@PathVariable String ItemId) {
        System.out.println("프론트에서 보낸 삭제 요청 ID: " + ItemId);
        cartService.removeCartItem(ItemId);
        return ResponseEntity.noContent().build();
    }

}
