package com.coderscampus.assignment13;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.UserRepository;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private AccountService accountService;
	
	@InjectMocks
	private UserService userService;
	
	@Test
	public void testAddAccountToUser() {
		User user = new User();
		user.setUserId(1L);
		Account account = new Account();
		
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		doNothing().when(accountService).saveAccount(any(Account.class));
		
		Optional<User> result = accountService.addAccountToUser(1L, account);
		
		assertTrue(result.isPresent());
		assertEquals(1, result.get().getAccounts().size());
	}

}
