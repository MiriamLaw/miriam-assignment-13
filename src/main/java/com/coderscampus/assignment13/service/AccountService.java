package com.coderscampus.assignment13.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private UserService userService;

	public Account saveAccount(Account account) {
		return accountRepository.save(account);
	}

	public Optional<User> addAccountToUser(Long userId, Account newAccount) {
		return userService.findById(userId).map(user -> {
			this.saveAccount(newAccount);
			user.getAccounts().add(newAccount);
			newAccount.getUsers().add(user);
			return Optional.of(userService.saveUser(user));
		}).orElse(Optional.empty());
	}

	public void updateAccountForUser(Long userId, Long accountId, Account updatedAccount) {
		User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		Account account = user.getAccounts().stream().filter(a -> a.getAccountId().equals(accountId)).findFirst()
				.orElseThrow(() -> new RuntimeException("Account not found"));

		account.setAccountName(updatedAccount.getAccountName());

		userService.saveUser(user);

	}

}
