package com.chatwithstranger.demo.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class User {
    @Id
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String lastLogin;

    private User() {
    }

    private User(String username, String password, String lastLogin) {
        this.username = username;
        this.password = password;
        this.lastLogin = lastLogin;
    }

    public static User createWithAllArgs(String username, String password, String lastLogin) {
        return new User(username, password, lastLogin);
    }

    public static User createWithoutLastLogin(String username, String password) {
        return new User(username, password, null);
    }

    public static User createInitialUser() {
        return new User(null, null, null);
    }
}
