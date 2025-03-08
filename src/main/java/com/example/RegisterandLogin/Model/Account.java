package com.example.RegisterandLogin.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Account")
public class Account {
    @Id
    @Column(nullable = false, unique = true)
    private String accountNumber;
    private  String accountName;
    private  Integer accountBalance;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
