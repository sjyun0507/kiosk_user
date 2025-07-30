package com.cafe_kiosk.kiosk_user.dto;

import com.cafe_kiosk.kiosk_user.domain.Cart;
import com.cafe_kiosk.kiosk_user.domain.Menu;
import com.cafe_kiosk.kiosk_user.domain.MenuOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO extends BaseEntityDTO {

    private Long cartId;
    private String phone;
    private MenuDTO menu;
    private MenuOptionDTO option;
    private String[] options;
    private Long quantity = 1L;
    private String sessionId; //*주문고객 식별용 Id 추가


    // 엔티티 → DTO
    public static CartDTO entityToDto(Cart cart) {
        return CartDTO.builder()
                .cartId(cart.getCartId())
                .sessionId(cart.getSessionId())
                .phone(cart.getPhone())
                .menu(MenuDTO.entityToDto(cart.getMenu())) // Menu 엔티티 → MenuDTO
                .option(cart.getOption() != null ? MenuOptionDTO.entityToDto(cart.getOption()) : null)
                .quantity(cart.getQuantity())
                .build();
    }

    // DTO → 엔티티
    public Cart dtoToEntity(Menu menu, MenuOption option) {
        return Cart.builder()
                .cartId(cartId)
                .phone(phone)
                .menu(menu)
                .option(option)
                .quantity(quantity)
                .sessionId(sessionId)
                .build();
    }
}
