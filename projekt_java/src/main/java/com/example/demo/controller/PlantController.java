package com.example.demo.controller;

import com.example.demo.data.PlantRequest;
import com.example.demo.entity.Plant;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PlantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plants")
@Transactional(readOnly = true)
public class PlantController {

    private final PlantService plantService;
    private final UserRepository userRepository;

    public PlantController(PlantService plantService, UserRepository userRepository) {
        this.plantService = plantService;
        this.userRepository = userRepository;
    }

    @GetMapping("/my")
    public List<Plant> getMyPlants(Authentication authentication) {
        String username = authentication.getName();
        return plantService.getPlantsForUser(username);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPlant(@RequestBody PlantRequest plantRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found: " + username);
        }

        Plant plant = new Plant();
        plant.name = plantRequest.getName();
        plant.description = plantRequest.getDescription();
        plant.user = user;
        plant.daysBetweenWatering = plantRequest.getDaysBetweenWatering();
        plant.favourite = plantRequest.isFavourite();
        plant.species = plantRequest.getSpecies();

        plantService.savePlant(plant);

        return ResponseEntity.ok("Plant added successfully");
    }


}

