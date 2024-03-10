package com.ragedev.orderahead.dtos;

import lombok.Data;

@Data
public class AuthenticateRequest
{
    private String email;
    String password;
}
