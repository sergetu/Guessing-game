package com.guessinggame.rest;

import com.guessinggame.db.entitys.User;
import com.guessinggame.rest.dto.UserResponse;
import com.guessinggame.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserRestController controller;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        controller = new UserRestController(userService);
    }

    @Test
    void testCreateUser_whenUserExists_returnsExistingUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Alice");

        when(userService.getUserByName("Alice")).thenReturn(existingUser);

        UserResponse response = controller.createUser("Alice");

        assertEquals(1L, response.getId());
        assertEquals("Alice", response.getName());

        verify(userService).getUserByName("Alice");
        verify(userService, never()).createUser(anyString());
    }

    @Test
    void testCreateUser_whenUserNotExists_createsAndReturnsUser() {
        when(userService.getUserByName("Bob")).thenReturn(null);

        User newUser = new User();
        newUser.setId(2L);
        newUser.setName("Bob");

        when(userService.createUser("Bob")).thenReturn(newUser);

        UserResponse response = controller.createUser("Bob");

        assertEquals(2L, response.getId());
        assertEquals("Bob", response.getName());

        verify(userService).getUserByName("Bob");
        verify(userService).createUser("Bob");
    }
}
