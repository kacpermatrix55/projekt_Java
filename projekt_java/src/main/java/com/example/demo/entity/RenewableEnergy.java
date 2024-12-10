package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class RenewableEnergy {
    @Id
    public Short year;

    @Column(nullable = false)
    public double allEnergy;

    @Column(nullable = false)
    public double electricity;

    @Column(nullable = false)
    public double heating;

    @Column(nullable = false)
    public double transport;
}
