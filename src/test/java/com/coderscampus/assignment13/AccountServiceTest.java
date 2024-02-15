package com.coderscampus.assignment13;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.repository.UserRepository;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.UserService;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private UserService userService;
    
    @MockBean
    private AccountRepository accountRepository;
    
    @MockBean
    private UserRepository userRepository;

    @Test
    public void testAddAccountToUser() {
        // Arrange
        User mockUser = new User();
        mockUser.setUserId(1L);
        Account account = new Account();
      
        when(userService.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userService.saveUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        // Act
        Optional<User> result = accountService.addAccountToUser(1L, account);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getAccounts().size());
        assertTrue(result.get().getAccounts().contains(account));
       
    }
    
    @Test
    public void testUpdateAccountForUser() {
        // Arrange
        User mockUser = new User();
        mockUser.setUserId(1L);
        Account existingAccount = new Account();
        existingAccount.setAccountId(1L);
        existingAccount.setAccountName("Initial Name");
        
        Account updatedAccount = new Account();
        updatedAccount.setAccountId(1L);
        updatedAccount.setAccountName("Updated Name");
        
        // Assuming mockUser's accounts include existingAccount
        mockUser.getAccounts().add(existingAccount);

        when(userService.findById(1L)).thenReturn(Optional.of(mockUser));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        // Act
        accountService.updateAccountForUser(1L, 1L, updatedAccount);

        // Assert
        assertEquals("Updated Name", existingAccount.getAccountName());
    }
    
    @Test
    public void testAddDuplicateAccountToUser() {
        // Arrange
        User mockUser = new User();
        mockUser.setUserId(1L);
        Account account = new Account();
        account.setAccountId(1L);
        mockUser.getAccounts().add(account);

        when(userService.findById(1L)).thenReturn(Optional.of(mockUser));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        Optional<User> result = accountService.addAccountToUser(1L, account);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getAccounts().size()); // Assuming your service prevents duplicates
    }


}
