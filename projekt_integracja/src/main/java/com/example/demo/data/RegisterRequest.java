package com.example.demo.data;

import lombok.Data;

import java.util.List;

@Data
public class RegisterRequest {
    public final String username;
    public final String password;
    public final List<String> access;
}
