package com.coderscampus.assignment13;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.UserService;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    
    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void testAddAccountToUser() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        Account account = new Account();
      
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        // Act
        Optional<User> result = accountService.addAccountToUser(1L, account);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getAccounts().size());
    }
}
