package com.cafe_kiosk.kiosk_user.dto;

import com.cafe_kiosk.kiosk_user.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long orderItemId;
    private OrdersDTO order;
    private MenuDTO menu;
    private MenuOptionDTO option;
    private String options;
    private Long quantity;
    private Long price;

    // Entity → DTO
    public static OrderItemDTO entityToDto(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .orderItemId(orderItem.getOrderItemId())
                .order(OrdersDTO.entityToDto(orderItem.getOrder()))
                .menu(MenuDTO.entityToDto(orderItem.getMenu()))
                .option(orderItem.getOption() != null ? MenuOptionDTO.entityToDto(orderItem.getOption()) : null)
                .options(orderItem.getOptions())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }

    // DTO → Entity
    public OrderItem dtoToEntity() {
        return OrderItem.builder()
                .orderItemId(orderItemId)
                .order(order.dtoToEntity())
                .menu(menu.dtoToEntity())
                .option(option != null ? option.dtoToEntity() : null)
                .options(options)
                .quantity(quantity)
                .price(price)
                .build();
    }
}
