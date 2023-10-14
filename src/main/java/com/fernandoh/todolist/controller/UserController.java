package com.fernandoh.todolist.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fernandoh.todolist.model.UserModel;
import com.fernandoh.todolist.repository.UserModelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserModelRepository userModelRepository;

    public UserController(UserModelRepository userModelRepository) {
        this.userModelRepository = userModelRepository;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserModel userModel) throws Exception {
        UserModel user = userModelRepository.findByUsername(userModel.getUsername())
                .orElseThrow(() -> new Exception("Usuário já existe"));

        String passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashed);

        UserModel userSaved = userModelRepository.save(userModel);
        return ResponseEntity.ok(userSaved);
    }
}
