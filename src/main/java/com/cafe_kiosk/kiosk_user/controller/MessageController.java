package com.cafe_kiosk.kiosk_user.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/message")
public class MessageController {

    @GetMapping("/send_sms")
    public void sendSms() {
        log.info("sendSms called");

    }
}
