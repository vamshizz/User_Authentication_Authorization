package com.example.RegisterandLogin.Model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "User")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private  String email;
    private  String password;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Account account;

}

