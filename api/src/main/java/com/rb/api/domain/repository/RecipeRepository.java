package com.rb.api.domain.repository;

import com.rb.api.domain.model.Category;
import com.rb.api.domain.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
}