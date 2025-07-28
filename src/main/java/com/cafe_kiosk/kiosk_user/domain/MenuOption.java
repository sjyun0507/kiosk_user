package com.cafe_kiosk.kiosk_user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu_option")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menuId", nullable = false)
    private Menu menu;

    @Column(nullable = false, length = 100)
    private String optionName;

    @Column(nullable = false)
    private Long optionPrice = 0L;

    @Column(length = 50)
    private String optionType;
}