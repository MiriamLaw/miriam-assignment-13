package com.coderscampus.assignment13.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public void delete(Long userId) {
		userRepo.deleteById(userId);
	}

	public User updateUser(Long userId, User updatedUser) {
		Optional<User> userOpt = findById(userId);
		if (userOpt.isPresent()) {
			User existingUser = userOpt.get();
			existingUser.setUsername(updatedUser.getUsername());
			if (updatedUser.getPassword() != null && !updatedUser.getPassword().trim().isEmpty()) {
				existingUser.setPassword(updatedUser.getPassword());
			}
			existingUser.setName(updatedUser.getName());

			Address updatedAddress = updatedUser.getAddress();
			if (updatedAddress != null) {
				Address existingAddress = existingUser.getAddress() != null ? existingUser.getAddress() : new Address();
				existingAddress.setAddressLine1(updatedAddress.getAddressLine1());
				existingAddress.setAddressLine2(updatedAddress.getAddressLine2());
				existingAddress.setCity(updatedAddress.getCity());
				existingAddress.setCountry(updatedAddress.getCountry());
				existingAddress.setRegion(updatedAddress.getRegion());
				existingAddress.setZipCode(updatedAddress.getZipCode());

				if (existingUser.getAddress() == null) {
					existingUser.setAddress(existingAddress);
					existingAddress.setUser(existingUser);

				}
			}
			return userRepo.save(existingUser);

		} else {
			return null;
		}
	}

}
