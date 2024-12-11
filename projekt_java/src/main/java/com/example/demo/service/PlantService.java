package com.example.demo.service;

import com.example.demo.entity.Plant;
import com.example.demo.entity.User;
import com.example.demo.repository.PlantRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantService {

    private final UserRepository userRepository;
    private final PlantRepository plantRepository;

    public PlantService(UserRepository userRepository, PlantRepository plantRepository) {
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;
    }

    public List<Plant> getPlantsForUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.getPlants();
    }

    public void savePlant(Plant plant) {
        plantRepository.save(plant);
    }



}



