package com.example.demo.controller;

import com.example.demo.data.PlantRequest;
import com.example.demo.entity.Plant;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PlantService;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/plants")
@Transactional(readOnly = true)
public class PlantController {
    private final PlantService plantService;
    private final UserRepository userRepository;

    private static void notifyPlantAdded(String plantName, String recipient) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("projektinzynieria2024@gmail.com", "rqsa twfl edtw ksvm");
            }
        });

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent("Plant " + plantName + " added successfully", "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("projektinzynieria2024@gmail.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject("Plant added");
        message.setContent(multipart);

        Transport.send(message);
    }

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
    public ResponseEntity<String> addPlant(@RequestBody PlantRequest plantRequest) throws MessagingException {

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

        notifyPlantAdded(plant.name, user.username);

        return ResponseEntity.ok("Plant added successfully");
    }


}

