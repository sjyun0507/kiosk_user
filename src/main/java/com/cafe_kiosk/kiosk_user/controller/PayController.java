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
import org.springframework.ui.Model;
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
@RequestMapping("/api/pay")
@CrossOrigin(origins = "http://localhost:5173")
public class PayController {
    private final MainService mainService;
    private final OrderService orderService;
    @Value("${com.tjfgusdh.toss.widgetClientKey}")
    private String widgetClientKey;

    @Value("${com.tjfgusdh.toss.widgetSecretKey}")
    private String widgetSecretKey;

    //     주문 생성
    @Operation(summary = "주문생성")
    @PostMapping(value = "/order")
    public ResponseEntity<OrdersDTO> createOrder(@RequestBody CreateOrderRequest request) {
        // 1) 전화번호로 유저 조회 또는 생성
        UserDTO userDTO = mainService.findOrCreateUserByPhone(request.getPhone());

        // 2) 주문 생성
        Orders order = mainService.createOrder(userDTO.getPhone(), request.getCartItems(), request.getOrderMethod());

        // 3) DTO 변환 후 반환
        OrdersDTO ordersDTO = OrdersDTO.entityToDto(order);
        return ResponseEntity.ok(ordersDTO);
    }

    @Operation(summary = "주문정보")
    @GetMapping(value = "/order/{orderId}")
    public ResponseEntity<?> getOrderInfo(@PathVariable Long orderId) {
        OrdersDTO order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

//    @GetMapping("/checkout")
//    public void checkout(Model model) {
//        log.info("checkout()...");
//
//        //주문 ID 생성
//        String orderId = orderService.getOrderId();
//        log.info("orderId: {}", orderId);
//        //더미 주문 추가
//        orderService.addDummyOrder(orderId);
//        //주문 정보 가져오기
//        OrderDTO orderDTO = orderService.getOrder(orderId);
//        log.info("order: {}", orderDTO);
//        model.addAttribute("order", orderDTO);
//        model.addAttribute("widgetClientKey", widgetClientKey);
//    }

    @Operation(summary = "결제")
    @PostMapping(value = "/confirm")
    public ResponseEntity<JSONObject> confirm(@RequestBody JSONObject jsonBody) throws Exception {
        JSONParser parser = new JSONParser();
        Long orderId;
        String amount;
        String paymentKey;
        String phone;

        JSONObject requestData = jsonBody;
        paymentKey = (String) requestData.get("paymentKey");
        orderId = (Long) requestData.get("orderId");
        amount = (String) requestData.get("amount");
        phone = (String) requestData.get("phone");

        mainService.findOrCreateUserByPhone(phone);
        long total = mainService.getCartItems().stream()
                .mapToLong(item -> item.getMenu().getPrice() * item.getQuantity())
                .sum();

        if (total != Integer.parseInt(amount)) {
            JSONObject response = new JSONObject();
            response.put("error", "INVALID_AMOUNT");
            response.put("message", "결제 금액이 실제 주문 금액과 일치하지 않습니다.");
            return ResponseEntity.status(400).body(response);

        }

        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);

        String encodedAuth = Base64.getEncoder().encodeToString((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + encodedAuth;

        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes(StandardCharsets.UTF_8));

        int code = connection.getResponseCode();
        InputStream responseStream = code == 200 ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();

//         결제 승인 성공시 DB 반영
        if (code == 200) {
            orderService.modifyPaymentKey(orderId, paymentKey);
            orderService.modifyOrderStatus(orderId, OrderStatus.COMPLETE);
        }

        return ResponseEntity.status(code).body(jsonObject);
    }
}
