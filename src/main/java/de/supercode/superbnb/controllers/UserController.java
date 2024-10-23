package de.supercode.superbnb.controllers;


import de.supercode.superbnb.dto.UserCreateDTO;
import de.supercode.superbnb.dto.UserDTO;
import de.supercode.superbnb.entities.AppUser;
import de.supercode.superbnb.repositories.UserRepository;
import de.supercode.superbnb.services.AuthentificationService;
import de.supercode.superbnb.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthentificationService authentificationService;

    //@PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() ) {
            AppUser currentUser = userRepository.findByEmail(authentication.getName()).orElseThrow();


            UserDTO userDTO = new UserDTO(currentUser.getId(), currentUser.getUsername(), currentUser.getEmail());
            return ResponseEntity.ok(userDTO);
        }


        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


     @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDTO> createUser( @Valid @RequestBody UserCreateDTO userCreatedDTO) {
        AppUser user = new AppUser();
        user.setUsername(userCreatedDTO.username());
        user.setEmail(userCreatedDTO.email());
        user.setPassword(userCreatedDTO.password());
        AppUser createdUser = userService.createUser(user);
        UserDTO responseDTO = new UserDTO(createdUser.getId(), createdUser.getUsername(), createdUser.getEmail());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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
