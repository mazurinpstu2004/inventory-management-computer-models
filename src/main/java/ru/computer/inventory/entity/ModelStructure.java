package ru.computer.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "model_structures")
@Data
public class ModelStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "model_id", referencedColumnName = "id", nullable = false)
    private Model model;

    @ManyToOne
    @JoinColumn(name = "component_id", referencedColumnName = "id", nullable = false)
    private Component component;

    @Column(name = "quantity")
    private Integer quantity;
}
