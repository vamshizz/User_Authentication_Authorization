package com.example.RegisterandLogin.Repository;

import com.example.RegisterandLogin.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,String> {

     Optional<Account> findByUserId(Integer userId);
}
