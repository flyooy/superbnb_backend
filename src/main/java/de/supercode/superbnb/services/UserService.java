package de.supercode.superbnb.services;

import de.supercode.superbnb.dto.UserDTO;
import de.supercode.superbnb.entities.AppUser;
import de.supercode.superbnb.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<UserDTO> getAllUsers(){
        List<AppUser> users = userRepository.findAll();
        return users.stream()
                .map(user->new UserDTO(user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    public AppUser createUser(AppUser user){
        return userRepository.save(user);
    }

    public void deleteUser(long id){
         userRepository.deleteById(id);
    }
}
