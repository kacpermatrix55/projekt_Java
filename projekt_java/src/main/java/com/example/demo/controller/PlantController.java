package com.example.demo.controller;

import com.example.demo.entity.Plant;
import com.example.demo.service.PlantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plants")
public class PlantController {

    @Autowired
    private PlantService plantService;

    @GetMapping("/my")
    public List<Plant> getMyPlants(Authentication authentication) {
        String username = authentication.getName();
        return plantService.getPlantsForLoggedInUser(username);
    }
}

