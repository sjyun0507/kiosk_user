package com.cafe_kiosk.kiosk_user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "admin_log")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLog extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminId", nullable = false)
    private Admin admin;

    @Column(length = 50)
    private String actionType;

    @Column(length = 50)
    private String targetTable;

    private Long targetId;
}
