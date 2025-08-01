package com.cafe_kiosk.kiosk_user.controller;

import com.cafe_kiosk.kiosk_user.domain.OrderStatus;
import com.cafe_kiosk.kiosk_user.dto.CartDTO;
import com.cafe_kiosk.kiosk_user.dto.UserDTO;
import com.cafe_kiosk.kiosk_user.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/pay")
public class PayController {

    private final MenuService menuService;
    private final OrderService orderService;
    private final SmsService smsService;
    private final CartService cartService;
    private final UserService userService;

    @Value("${com.tjfgusdh.toss.widgetClientKey}")
    private String widgetClientKey;

    @Value("${com.tjfgusdh.toss.widgetSecretKey}")
    private String widgetSecretKey;

//    @GetMapping("/")
//    public String index() {
//        return "redirect:/pay/checkout";
//
//    }
//
//    @GetMapping("/checkout")
//    public void checkout(Model model) {
//        log.info("checkout()...");
//
//        //주문 ID 생성
//        String orderId = orderService.getOrderId();
//        log.info("orderId: {}", orderId);
//
//        //주문 정보 가져오기
//        String orderDTO = orderService.getOrderId();
//        model.addAttribute("order", orderDTO);
//        model.addAttribute("widgetClientKey", widgetClientKey);
//    }

    @PostMapping("/confirm")
    public ResponseEntity<JSONObject> confirm(@RequestBody String jsonBody) throws Exception {
        JSONParser parser = new JSONParser();
        String tossOrderId;
        String amount;
        String paymentKey;
        Long orderId;

        List<CartDTO> cartDTOList = cartService.getCartItems();

        String menuList = cartDTOList.stream()
                .map(cart -> cart.getMenu().getName())
                .collect(Collectors.joining(", "));

        try {
            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
            paymentKey = (String) requestData.get("paymentKey");
            tossOrderId = (String) requestData.get("orderId"); // Toss 기준 orderId (문자열)
            amount = (String) requestData.get("amount");

            // tossOrderId → 내부 DB의 Long 타입 orderId 조회
            orderId = orderService.getOrderIdByTossOrderId(tossOrderId);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (orderService.getOrder(orderId).getTotalAmount() != Integer.parseInt(amount)) {
            JSONObject response = new JSONObject();
            response.put("message", "결제 금액 위조");
            return ResponseEntity.status(400).body(response);
        }
        String phone = orderService.getPhoneByOrderId(orderId);

        JSONObject obj = new JSONObject();
        obj.put("orderId", tossOrderId);
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
        log.info(obj.toString());
        int code = connection.getResponseCode();
        log.info("code: " + code);
        InputStream responseStream = code == 200 ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();

//         결제 승인 성공시 DB 반영
        if (code == 200) {
            log.info("결제 승인 성공시 DB 반영");

            orderService.modifyPaymentKey(orderId, paymentKey);
            orderService.modifyOrderStatus(orderId, OrderStatus.COMPLETE);

            UserDTO userDTO = userService.findOrCreateUserByPhone(phone);
            Long usedPoints = orderService.getOrder(orderId).getUsedPoint();
            Long earnedPoint = orderService.getOrder(orderId).getEarnedPoint();
            userDTO.setPoints(userDTO.getPoints() + earnedPoint-usedPoints);
            userService.userSave(userDTO);

            log.info("주문완료 승인후 테스트");
            String text = ("주문이 완료되었습니다 \n주문번호: " + orderId +"\n주문금액: " + amount +"업체명: BEANS COFFEE"+"\n주문내역: " + menuList);
//          smsService.sendSms(phone, text);
            cartService.clearCart();
        }

        // 전화번호로 유저 조회 또는 생성

        return ResponseEntity.status(code).body(jsonObject);
    }

}
