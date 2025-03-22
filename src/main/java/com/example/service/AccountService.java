package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.InvalidInputException;
import com.example.exception.UnauthorizedException;
import com.example.exception.UserAlreadyExistsException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account registerAccount(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new InvalidInputException("Username cannot be blank.");
        }

        if (password == null || password.length() < 4) {
            throw new InvalidInputException("Password must be at least 4 characters long.");
        }

        if (accountRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists.");
        }

        return accountRepository.save(new Account(username, password));
    }

    public Account loginAccount(String username, String password) {
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isEmpty() || !account.get().getPassword().equals(password)) {
            throw new UnauthorizedException("Invalid username or password.");
        }
        return account.get();
    }
}
