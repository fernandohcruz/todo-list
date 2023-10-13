package com.fernandoh.todolist.controller;

import com.fernandoh.todolist.model.UserModel;
import com.fernandoh.todolist.repository.UserModelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserModelRepository userModelRepository;

    public UserController(UserModelRepository userModelRepository) {
        this.userModelRepository = userModelRepository;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserModel userModel) {
        Optional<UserModel> user = userModelRepository.findByUsername(userModel.getUsername());
        if(user.isPresent())
            return ResponseEntity.badRequest().body("Usuário já existe");

        UserModel userSaved = userModelRepository.save(userModel);
        System.out.println(userSaved);
        return ResponseEntity.ok(userSaved);
    }
}
