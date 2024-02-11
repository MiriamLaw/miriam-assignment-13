package com.coderscampus.assignment13.web;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String getCreateUser(ModelMap model) {

		model.put("user", new User());

		return "register";
	}

	@PostMapping("/register")
	public String postCreateUser(User user) {
		System.out.println(user);
		userService.saveUser(user);
		return "redirect:/register";
	}

	@GetMapping("/users")
	public String getAllUsers(ModelMap model) {
		Set<User> users = userService.findAll();

		model.put("users", users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
		}

		return "users";
	}

	@GetMapping("/users/{userId}")
	public String getOneUser(ModelMap model, @PathVariable Long userId) {
		Optional<User> userOpt = userService.findById(userId);
		if (userOpt.isPresent()) {
			User user = userOpt.get();

			if (user.getAddress() == null) {
				user.setAddress(new Address());
			}

			model.put("users", Arrays.asList(user));
			model.put("user", user);
			return "users";
		} else {
			model.put("errorMessage", "User not found");
			return "redirect:/users";
		}

	}

	@PostMapping("/users/{userId}")
	public String postOneUser(User user) {
		userService.saveUser(user);
		return "redirect:/users/" + user.getUserId();
	}

	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser(@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}

	@PostMapping("/users/{userId}/updateUser")
	public String updateUserAccount(@PathVariable Long userId, @ModelAttribute User user) {
		userService.updateUser(userId, user);
		return "redirect:/users/" + userId;
	}

	@GetMapping("/users/{userId}/accounts/create")
	public String showCreateAccountForm(@PathVariable Long userId, Model model) {
		model.addAttribute("account", new Account());
	    model.addAttribute("userId", userId);
	    return "account"; 
	}

	@PostMapping("/users/{userId}/accounts")
	public String createAccountForUser(@PathVariable Long userId, @Valid @ModelAttribute("account") Account account, BindingResult result) {
	    if (result.hasErrors()) {
	        return "account";
	    }
	    userService.addAccountToUser(userId, account);
	    return "redirect:/users/" + userId;
	}

	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getAccountDetails(@PathVariable Long userId, @PathVariable Long accountId, Model model) {
		 Optional<User> userOpt = userService.findById(userId);
		 if (userOpt.isPresent()) {
			 User user = userOpt.get();
					 Account account = user.getAccounts().stream()
					 .filter(a -> accountId.equals(a.getAccountId()))
					 .findFirst()
					 .orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));
			 
			 model.addAttribute("userId", userId);
			 model.addAttribute("account", account);
			 model.addAttribute("accountName", account.getAccountName());
			 
			 return "account";
		 } else {
			 throw new IllegalArgumentException("Invalid user ID");
		 }
		
	}
	
	@PostMapping("/users/{userId}/accounts/{accountId}")
	public String updateAccountDetails(@PathVariable Long userId, @PathVariable Long accountId, @ModelAttribute("account") Account account, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        model.addAttribute("userId", userId);
	        model.addAttribute("account", account); 
	        return "account"; 
	    }
	    userService.updateAccountForUser(userId, accountId, account); 
	    return "redirect:/users/" + userId + "/accounts/" + accountId;
	}


}
