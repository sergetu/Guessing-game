package com.guessinggame.services;

import com.guessinggame.db.entitys.User;
import com.guessinggame.db.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;

    public User createUser(String newUserName) {
        if (userRepository.findByName(newUserName).isPresent())
            return null;
        User newUser = new User();
        newUser.setName(newUserName);
        return userRepository.save(newUser);
    }

    public User getUserByName(String userName) {
        Optional<User> user = userRepository.findByName(userName);
        return user.orElse(null);

    }

}
