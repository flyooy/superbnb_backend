package de.supercode.superbnb.services;

import de.supercode.superbnb.dto.UserDTO;
import de.supercode.superbnb.entities.AppUser;
import de.supercode.superbnb.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Optional<AppUser> getUserById(long id){
       return userRepository.findById(id);
    }
    public List<UserDTO> getAllUsers(){
        List<AppUser> users = userRepository.findAll();
        return users.stream()
                .map(user->new UserDTO(user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    public AppUser createUser(AppUser user){
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("User not found with id " + id);
        }
    }
}
