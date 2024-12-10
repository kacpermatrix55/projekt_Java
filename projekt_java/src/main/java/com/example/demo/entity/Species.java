package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class Species {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public short id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public int difficulty;

    @Column(nullable = false)
    public int water;

    @Column(nullable = false)
    public int light;
}
