package com.coderscampus.assignment13.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	public Set<User> findAll() {
		return userRepo.findAllUsersWithAccountsAndAddresses();
	}

	public Optional<User> findById(Long userId) {
		return userRepo.findByIdWithAccountsAndAddress(userId);
	}

	public User saveUser(User user) {
		return userRepo.save(user);
	}

	public Optional<User> addAccountToUser(Long userId, Account newAccount) {
		Optional<User> userOpt = findById(userId);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.getAccounts().add(newAccount);
			newAccount.getUsers().add(user);
			return Optional.of(saveUser(user));
		}
		return Optional.empty();

	}

	public void delete(Long userId) {
		userRepo.deleteById(userId);
	}

	public User updateAddress(Long userId, Address address) {
		Optional<User> userOpt = findById(userId);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			Address userAddress = user.getAddress();
			if (userAddress == null) {
				userAddress = new Address();
				user.setAddress(userAddress);
				userAddress.setUser(user);
			}

			userAddress.setAddressLine1(address.getAddressLine1());
			userAddress.setAddressLine2(address.getAddressLine2());
			userAddress.setCity(address.getCity());
			userAddress.setCountry(address.getCountry());
			userAddress.setRegion(address.getRegion());
			userAddress.setZipCode(address.getZipCode());
			return userRepo.save(user);
		} else {
			return null;
		}
	}
}
