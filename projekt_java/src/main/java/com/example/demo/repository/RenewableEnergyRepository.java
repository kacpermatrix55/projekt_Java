package com.example.demo.repository;

import com.example.demo.entity.RenewableEnergy;
import org.springframework.data.repository.ListCrudRepository;

public interface RenewableEnergyRepository extends ListCrudRepository<RenewableEnergy, Short>  {
}
