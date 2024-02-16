package com.coderscampus.assignment13;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.coderscampus.assignment13.domain.Address;
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

	@Test
	public void testUpdateUser() {
		// Arrange
		Long userId = 1L;
		User existingUser = new User();
		existingUser.setUserId(userId);
		existingUser.setUsername("oldUsername");

		User updatedUser = new User();
		updatedUser.setUsername("newUsername");

		when(userRepository.findByIdWithAccountsAndAddress(userId)).thenReturn(Optional.of(existingUser));
		when(userService.saveUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		User resultUser = userService.updateUser(userId, updatedUser);

		// Assert
		assertNotNull(resultUser, "Resulting user should not be null");
		assertEquals(userId, resultUser.getUserId(), "User ID should match");
		assertEquals("newUsername", resultUser.getUsername(), "Username should be updated to 'newUsername'");
	}

	@Test
	public void testUpdateUserWithAddress() {
		// Arrange
		Long userId = 1L;
		User existingUser = new User();
		existingUser.setUserId(userId);
		existingUser.setUsername("oldUsername");
		Address existingAddress = new Address();
		existingAddress.setAddressLine1("Old Address Line 1");
		existingUser.setAddress(existingAddress);

		User updatedUser = new User();
		updatedUser.setUsername("newUsername");
		Address updatedAddress = new Address();
		updatedAddress.setAddressLine1("New Address Line 1");
		updatedUser.setAddress(updatedAddress);

		when(userRepository.findByIdWithAccountsAndAddress(userId)).thenReturn(Optional.of(existingUser));
		when(userService.saveUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		User resultUser = userService.updateUser(userId, updatedUser);

		// Assert
		assertNotNull(resultUser, "Resulting user should not be null");
		assertEquals("newUsername", resultUser.getUsername(), "Username should be updated to 'newUsername'");
		assertNotNull(resultUser.getAddress(), "User should have an address");
		assertEquals("New Address Line 1", resultUser.getAddress().getAddressLine1(),
				"Address Line 1 should be updated");
	}

}
