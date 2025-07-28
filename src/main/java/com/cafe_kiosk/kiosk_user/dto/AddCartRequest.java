package com.cafe_kiosk.kiosk_user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartRequest {
    private String phone;       // 고객 전화번호
    private Long menuId;        // 메뉴 ID
    private Long optionId;      // 옵션 ID (nullable)
    private int quantity;       // 수량
}