package de.supercode.superbnb.repositories;

import de.supercode.superbnb.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser,Long> {
}
