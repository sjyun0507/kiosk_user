# ☕ Cafe Kiosk

스마트한 무인 주문 시스템, 카페 키오스크 프로젝트입니다. 
<p>실제 키오스크처럼 사용자/관리자 화면이 분리되어 있으며, 해당 프로젝트는 사용자화면 키오스크 프로젝트 입니다.</p>

<br />

## 🗓 프로젝트 개요

- 프로젝트명 : BEANS COFFEE KIOSK
- 키오스크 개발인원 : 2명
- 프로젝트 기간: 2025.07.25 ~ 2025.08.02 (9일)
- 목적 : 실제 카페 키오스크 UX 분석 및 기능 구현을 통한 풀스택 개발 역량 강화
<br />

## 👥 역할 분담

| 이름 | 담당 역할 |
|------|------------|
| Rachel (Seojeong Yun) | 프론트엔드, UI 설계, 결제 기능 연동 |
| seolhyeono | 백엔드, DB 설계, API 개발 |
<br />

## 📸 Preview
<p>직관적인 UI와 실제 키오스크 동작을 모방한 커피 주문 웹앱입니다. 반응형 UI 적용으로 다양한 디바이스 대응가능합니다.</p>

![cafe Kiosk](https://github.com/user-attachments/assets/d67d104d-82b1-4a33-8009-1868ddc295f8)


<br />

## 🛠️ Tech Stack

- **Frontend**: React, JavaScript, Tailwind CSS
- **Backend**: Java, Spring Boot
- **Database**: MariaDB
- **ORM**: JPA 
- **Tools**: Git, GitHub, Notion, Swagger

<br />

## 🚀 주요 기능

### 초기 화면
- 버튼 클릭 시 메인 화면으로 이동
<img width="500" alt="Image" src="https://github.com/user-attachments/assets/c905818d-520f-4edb-9ac9-2dbacd54d862" />

### 메인 키오스크 화면
- 메뉴 조회 및 선택
- 옵션 선택 및 장바구니 담기
- 장바구니 수량 변경 및 삭제, 재고 기반 주문 제한
- 타이머 설정 : 2분 뒤 주문 유무 확인
<img width="500" alt="Image" src="https://github.com/user-attachments/assets/16eabdc7-ca60-43c0-985d-ffcd6731d42a" />
<img width="500" height="916" alt="Image" src="https://github.com/user-attachments/assets/69c68b34-0d2a-4d29-9cd1-2982779249b2" />

### 주문 및 결제
- 결제 기능 (토스 연동)
- 포인트 적립 및 사용
<img width="500" alt="Image" src="https://github.com/user-attachments/assets/530dfb64-566a-4f62-8f7d-419e8b096ec1" />

### 주문 내역 확인
- 결제 성공 화면
- 금액, 주문번호, 주문날짜 확인 후 초기화면으로 이동
<img width="500" alt="Image" src="https://github.com/user-attachments/assets/c708ebf6-27d2-48b5-b345-9d035f62ab36" />


<br />

### 📌 시스템 아키텍처
- 3-Tier 구조
- Restful API 기반 클라이언트-서버 통신

<br />
