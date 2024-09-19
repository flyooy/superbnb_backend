package de.supercode.superbnb.controllers;


import de.supercode.superbnb.dto.UserCreateDTO;
import de.supercode.superbnb.dto.UserDTO;
import de.supercode.superbnb.entities.AppUser;
import de.supercode.superbnb.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser( @Valid @RequestBody UserCreateDTO userCreatedDTO) {
        AppUser user = new AppUser();
        user.setUsername(userCreatedDTO.username());
        user.setEmail(userCreatedDTO.email());
        user.setPassword(userCreatedDTO.password());
        AppUser createdUser = userService.createUser(user);
        UserDTO responseDTO = new UserDTO(createdUser.getUsername(), createdUser.getEmail());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser (@PathVariable Long id){
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
