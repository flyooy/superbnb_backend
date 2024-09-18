package de.supercode.superbnb.repositories;

import de.supercode.superbnb.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property,Long> {
    List<Property> findAllByAvailabilityIsTrue();
}
