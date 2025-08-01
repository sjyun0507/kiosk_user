package com.cafe_kiosk.kiosk_user.dto;


import com.cafe_kiosk.kiosk_user.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {

    private Long menuId;
    private CategoryDTO category;
    private String name;
    private Long price;
    private String imageUrl;
    private Long stock = 0L;
    private Boolean isSoldOut = false;
    private LocalDateTime createdAt;

    public static MenuDTO entityToDto(Menu menu) {
        return MenuDTO.builder()
                .menuId(menu.getMenuId())
                .category(CategoryDTO.entityToDto(menu.getCategory())) // Category → CategoryDTO
                .createdAt(menu.getCreatedAt())
                .name(menu.getName())
                .price(menu.getPrice())
                .imageUrl(menu.getImageUrl())
                .stock(menu.getStock())
                .isSoldOut(menu.getIsSoldOut())
                .build();
    }

    public Menu dtoToEntity() {
        return Menu.builder()
                .menuId(menuId)
                .category(category.dtoToEntity())
                .name(name)
                .price(price)
                .imageUrl(imageUrl)
                .stock(stock)
                .isSoldOut(isSoldOut)
                .build();
    }
}
