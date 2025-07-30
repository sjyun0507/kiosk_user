package com.cafe_kiosk.kiosk_user.service;

import com.cafe_kiosk.kiosk_user.domain.Orders;
import com.cafe_kiosk.kiosk_user.dto.*;

import java.util.List;
import java.util.Optional;

public interface MainService {
    List<MenuDTO> allMenus();
    List<CategoryDTO> getAllCategories();
    List<MenuDTO> getMenusByCategory(Long categoryId);
    MenuDTO getMenu(Long menuId);
    List<MenuOptionDTO> getOptions(Long menuId);
    List<CartDTO> getCartItems();
    void updateCartQuantity(Long cartItemId, Long newQuantity);
    void removeCartItem(Long cartItemId);
    void clearCart(); // 주문 완료 후
//    OrdersDTO placeOrder(OrderRequest request);
    OrdersDTO getOrder(Long orderId);
    CartDTO addToCart(AddCartRequest request);
    UserDTO findOrCreateUserByPhone(String phone);
    UserDTO getUser(String phone);


    Orders createOrder(String phone, List<CartDTO> cartItems, String orderMethod);
}
