package com.example.demo.data;

import lombok.Data;

@Data
public class LoginRequest {
    public final String username;
    public final String password;
}
