package com.cafe_kiosk.kiosk_user.domain;

import com.cafe_kiosk.kiosk_user.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 20, nullable = false, unique = true)
    private String phone;

    private Long points = 0L;

}
