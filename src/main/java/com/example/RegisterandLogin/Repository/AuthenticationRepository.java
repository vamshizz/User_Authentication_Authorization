package com.example.RegisterandLogin.Repository;

import com.example.RegisterandLogin.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String s);
}
