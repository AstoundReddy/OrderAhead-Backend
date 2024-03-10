package com.ragedev.orderahead.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    String token;
    String firstName;
    Integer userId;
}
