package com.chatwithstranger.demo.service;

import com.chatwithstranger.demo.user.User;
import com.chatwithstranger.demo.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void signupUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        User userToUpdate = userRepository.getOne(user.getUsername());
        userToUpdate.setLastLogin(user.getLastLogin());
        userRepository.save(userToUpdate);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        try {
            return userRepository.findUserByUsername(username);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findUserByUsernameAndPassword(String username, String password) {
        try {
            return userRepository.findUserByUsernameAndPassword(username, password);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
