package com.cafe_kiosk.kiosk_user.service;

import com.cafe_kiosk.kiosk_user.domain.*;
import com.cafe_kiosk.kiosk_user.dto.*;
import com.cafe_kiosk.kiosk_user.repository.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainServiceImpl implements MainService {

    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    // 1. 카테고리 전체 조회
    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::entityToDto)
                .collect(Collectors.toList());
    }

    // 2. 카테고리별 메뉴 조회
    @Override
    public List<MenuDTO> getMenusByCategory(Long categoryId) {
        return menuRepository.findByCategory_CategoryId(categoryId)
                .stream()
                .map(MenuDTO::entityToDto)
                .collect(Collectors.toList());
    }

    // 3. 메뉴 상세 조회
    @Override
    public MenuDTO getMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
        return MenuDTO.entityToDto(menu);
    }

    // 4. 메뉴 옵션 조회
    @Override
    public List<MenuOptionDTO> getOptions(Long menuId) {
        return menuOptionRepository.findByCategory_CategoryId(menuId)
                .stream()
                .map(MenuOptionDTO::entityToDto)
                .collect(Collectors.toList());
    }

    //     5. 장바구니 아이템 추가
    @Transactional
    public CartDTO addToCart(AddCartRequest request) {
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다. ID: " + request.getMenuId()));

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        Cart cart = new Cart();
        cart.setMenu(menu);
        cart.setOptions(request.getOptions());  // 배열 그대로 세팅
        cart.setQuantity(request.getQuantity());
        cartRepository.save(cart);

        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getCartId());
        dto.setOptions((cart.getOptions()));  // String[]
        dto.setQuantity(cart.getQuantity());

        return dto;
    }



    @Override
    public UserDTO getUser(String phone) {
        User user = userRepository.findByPhone(phone);
        return UserDTO.entityToDto(user);
    }

    @Override
    public List<MenuDTO> allMenus() {
            return menuRepository.findAll()
                    .stream()
                    .map(MenuDTO::entityToDto)
                    .collect(Collectors.toList());
    }

    // 6. 장바구니 아이템 리스트 조회
    @Override
    public List<CartDTO> getCartItems() {
        return cartRepository.findAll()
                .stream()
                .map(CartDTO::entityToDto)
                .collect(Collectors.toList());
    }



    // 7. 장바구니 아이템 수량 변경
//    @Transactional
    @Override
    public void updateCartQuantity(Long cartItemId, Long newQuantity) {
        Cart cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 아이템을 찾을 수 없습니다."));
        cartItem.setQuantity(newQuantity);
        cartRepository.save(cartItem);
    }

    // 8. 장바구니 아이템 삭제
    @Transactional
    @Override
    public void removeCartItem(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    // 9. 장바구니 전체 비우기
    @Transactional
    @Override
    public void clearCart() {
        cartRepository.deleteAll();
    }

    // 10. 주문 상세 조회
    @Override
    public OrdersDTO getOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 주문 아이템 조회
        List<OrderItem> items = orderItemRepository.findByOrder_OrderId(orderId);

        return OrdersDTO.entityToDto(order);
    }
}
