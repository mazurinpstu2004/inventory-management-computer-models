package ru.computer.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "models")
@Data
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
