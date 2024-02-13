package com.coderscampus.assignment13;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.UserRepository;
import com.coderscampus.assignment13.service.UserService;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository; 

    @Test
    public void testFindUserById() {
        // Arrange
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setUsername("testUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> foundUser = userService.findById(1L);

        // Assert
        assertNotNull(foundUser);
        foundUser.ifPresent(user -> {
            assertNotNull(user.getUsername());
        });
    }
}
