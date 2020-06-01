package com.chatwithstranger.demo.service;

import com.chatwithstranger.demo.user.User;
import java.util.Optional;

public interface UserService {

  void saveUser(User user);

  void updateUser(User user);

  Optional<User> findUserByUsername(String username);

  Optional<User> findUserByUsernameAndPassword(String username, String password);
}
