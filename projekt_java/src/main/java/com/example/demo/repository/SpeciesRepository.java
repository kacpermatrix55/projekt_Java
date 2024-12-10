package com.example.demo.repository;

import com.example.demo.entity.Species;
import org.springframework.data.repository.ListCrudRepository;

public interface SpeciesRepository extends ListCrudRepository<Species, Short>  {
}
