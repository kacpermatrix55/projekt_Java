package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "plant")
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public int species;

    @Column(nullable = false)
    public boolean favourite;

    @Column(nullable = false)
    public String description;

    @Column(nullable = false)
    public int daysBetweenWatering;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;
}
