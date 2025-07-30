package com.cafe_kiosk.kiosk_user.controller;

import com.cafe_kiosk.kiosk_user.domain.OrderStatus;
import com.cafe_kiosk.kiosk_user.dto.OrdersDTO;
import com.cafe_kiosk.kiosk_user.service.OrderService;
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

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/pay")
public class PayController {

    private final OrderService orderService;

    @Value("${com.tjfgusdh.toss.widgetClientKey}")
    private String widgetClientKey;

    @Value("${com.tjfgusdh.toss.widgetSecretKey}")
    private String widgetSecretKey;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderInfo(@PathVariable Long orderId) {
        OrdersDTO order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/confirm")
    public ResponseEntity<JSONObject> confirm(@RequestBody String jsonBody) throws Exception {
        JSONParser parser = new JSONParser();
        Long orderId;
        String amount;
        String paymentKey;

        try {
            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
            paymentKey = (String) requestData.get("paymentKey");
            orderId = (Long) requestData.get("orderId");
            amount = (String) requestData.get("amount");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (orderService.getOrder(orderId).getTotalAmount() != Integer.parseInt(amount)) {
            JSONObject response = new JSONObject();
            response.put("message", "결제 금액 위조");
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
            orderService.modifyOrderStatus(orderId, OrderStatus.payReceive);
        }

        return ResponseEntity.status(code).body(jsonObject);
    }
}
