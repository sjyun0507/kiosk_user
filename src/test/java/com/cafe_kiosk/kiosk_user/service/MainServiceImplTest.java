package com.cafe_kiosk.kiosk_user.service;

import com.cafe_kiosk.kiosk_user.dto.*;
import com.cafe_kiosk.kiosk_user.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MainServiceImplTest {
    @Autowired
    private MainService mainService;
    @Autowired
    private CartRepository cartRepository;

    @Test
    void getAllCategories() {
        List<CategoryDTO> categoryDTOs = mainService.getAllCategories();
        categoryDTOs.forEach(System.out::println);
    }
    @Test
    void getMenusByCategory() {
        Long categoryId = 1L;  // 테스트용 카테고리 ID를 넣으세요.
        List<MenuDTO> menuDTOs = mainService.getMenusByCategory(categoryId);
        menuDTOs.forEach(System.out::println);
    }
    @Test
    void getMenuDetail() {
        Long menuId = 1L;  // 실제 DB에 존재하는 메뉴 ID
        MenuDTO menuDTO = mainService.getMenu(menuId);
        System.out.println(menuDTO);
    }
    @Test
    void getOptionsForMenu() {
        Long menuId = 1L; // 실제 존재하는 메뉴 ID
        List<MenuOptionDTO> options = mainService.getOptions(menuId);
        options.forEach(System.out::println);
    }

    @Test
    void addToCartTest() {
        AddCartRequest request = new AddCartRequest();
//        request.setPhone("01012345678");
        request.setMenuId(1L);       // 실제 존재하는 메뉴 ID로 변경하세요
        request.setOptionId(null);   // 옵션 없을 때 null, 옵션 있을 땐 값 넣기
        request.setQuantity(2);

        // 1) 새 아이템 추가
        CartDTO firstAdd = mainService.addToCart(request);
        System.out.println("첫 추가: " + firstAdd);

        // 2) 같은 아이템 다시 추가 (수량 증가 기대)
        CartDTO secondAdd = mainService.addToCart(request);
        System.out.println("두 번째 추가: " + secondAdd);
    }

    @Test
    void getCartItemsTest() {
        List<CartDTO> cartDTOS = mainService.getCartItems();
        cartDTOS.forEach(System.out::println);
    }

    @Test
    void removeCartItemTest() {
        Long cartId = 5L;
        mainService.removeCartItem(cartId);
    }

    @Test
    void clearCartTest() {
        mainService.clearCart();
    }

    @Test
    void getOrdersTest() {
        Long orderId = 1L;
        OrdersDTO ordersDTO = mainService.getOrder(orderId);
    }
}