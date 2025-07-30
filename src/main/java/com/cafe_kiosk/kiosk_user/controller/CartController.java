package com.cafe_kiosk.kiosk_user.controller;

import com.cafe_kiosk.kiosk_user.domain.OrderStatus;
import com.cafe_kiosk.kiosk_user.domain.Orders;
import com.cafe_kiosk.kiosk_user.dto.*;
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {
    private final MainService mainService;

    @Operation(summary = "장바구니담기")
    @PostMapping(value="/")
    public ResponseEntity<CartDTO> addCart(@RequestBody AddCartRequest cart) {
        log.info(cart);
        log.info("Session ID: {}", cart.getSessionId());
        CartDTO result = mainService.addToCart(cart); // 결과 받아오기
        return ResponseEntity.ok(result);

    }


}
