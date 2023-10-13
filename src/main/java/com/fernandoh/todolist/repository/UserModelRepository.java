package com.fernandoh.todolist.repository;

import com.fernandoh.todolist.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserModelRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByUsername(String userName);
}
