package com.rb.api.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recipes")
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", unique = true, nullable = false)
    @NotNull
    private MenuItem menuItem;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String instructions;

    @PositiveOrZero
    @Column(nullable = false)
    private int prepTimeMinutes;

    
    public Recipe(MenuItem menuItem, String instructions, int prepTimeMinutes) {
        this.menuItem = menuItem;
        this.instructions = instructions;
        this.prepTimeMinutes = prepTimeMinutes;
    }

    public void updateDetails(String newInstructions, int newPrepTimeMinutes) {
        if (newInstructions == null || newInstructions.isBlank()) {
            throw new IllegalArgumentException("As instruções não podem ser vazias.");
        }
        if (newPrepTimeMinutes < 0) {
            throw new IllegalArgumentException("O tempo de preparação não pode ser negativo.");
        }
        this.instructions = newInstructions;
        this.prepTimeMinutes = newPrepTimeMinutes;
    }
}