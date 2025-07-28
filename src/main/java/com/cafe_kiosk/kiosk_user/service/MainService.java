package com.cafe_kiosk.kiosk_user.service;

import com.cafe_kiosk.kiosk_user.dto.*;

import java.util.List;

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
//  OrdersDTO placeOrder(OrderRequest request);
    OrdersDTO getOrder(Long orderId);
    CartDTO addToCart(AddCartRequest request);
    UserDTO getUser(Long userId);
}
