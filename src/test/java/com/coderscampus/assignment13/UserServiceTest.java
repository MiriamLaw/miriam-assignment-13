package com.coderscampus.assignment13;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

        when(userRepository.findByIdWithAccountsAndAddress(1L)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> foundUser = userService.findById(1L);

        // Assert
        assertTrue(foundUser.isPresent(), "User should be found");
        assertEquals(mockUser.getUserId(), foundUser.get().getUserId(), "User ID should match");
        assertEquals(mockUser.getUsername(), foundUser.get().getUsername(), "Username should match");
        assertNotNull(foundUser);
        foundUser.ifPresent(user -> {
            assertNotNull(user.getUsername());
        });
    }
    
    @Test
    public void testDelete() {
        // Arrange
        Long userIdToDelete = 1L;

        doNothing().when(userRepository).deleteById(userIdToDelete);

        // Act
        userService.delete(userIdToDelete);

        // Assert
        verify(userRepository, times(1)).deleteById(userIdToDelete);
    }
}
