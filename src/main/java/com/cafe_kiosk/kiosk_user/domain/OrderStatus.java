package com.cafe_kiosk.kiosk_user.domain;

public enum OrderStatus {
    orderFail, // 주문실패
    orderReceive, // 주문접수
    payReceive, // 입금확인
    shippingPrepare, // 배송준비
    shippingDelay, // 배송대기
    shippingProgress, // 배송중
    shippingComplete, // 배송완료
    orderCancel, // 주문취소
    exchangeRequest, // 교환요청
    exchangeComplete, // 교환완료
    refundRequest, // 환불요청
    refundComplete // 환불완료
}
