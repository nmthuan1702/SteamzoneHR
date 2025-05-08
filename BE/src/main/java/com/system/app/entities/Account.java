package com.system.app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountID", nullable = false)
    private Integer id;

    @Size(max = 20)
    @Column(name = "Username", length = 20)
    private String username;

    @Size(max = 50)
    @Column(name = "Email", length = 50)
    private String email;

    @Lob
    @Column(name = "Password")
    private String password;

}