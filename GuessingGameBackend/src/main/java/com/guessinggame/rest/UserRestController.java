package com.guessinggame.rest;

import com.guessinggame.db.entitys.User;
import com.guessinggame.rest.dto.UserResponse;
import com.guessinggame.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserRestController {
    UserService userService;

    @PostMapping("/get-or-create")
    public UserResponse createUser(@RequestParam String userName) {
        User user = userService.getUserByName(userName);
        if (user == null)
            user = userService.createUser(userName);
        return new UserResponse(user.getId(), user.getName());
    }
}
