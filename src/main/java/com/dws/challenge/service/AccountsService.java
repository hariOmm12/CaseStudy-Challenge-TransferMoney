package com.dws.challenge.service;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.InsufficientBalanceException;
import com.dws.challenge.repository.AccountsRepository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountsService {

	@Getter
	private final AccountsRepository accountsRepository;

	private final NotificationService notificationService;

	private final Lock lock = new ReentrantLock();

	@Autowired
	public AccountsService(AccountsRepository accountsRepository, NotificationService notificationService) {
		this.accountsRepository = accountsRepository;
		this.notificationService = notificationService;
	}

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}

	@Transactional
	public void transferMoney(String accountFromId, String accountToId, BigDecimal amount) { 

		lock.lock();
		try {
			// Get accounts
			final Account accountFrom = accountsRepository.getAccount(accountFromId);
			final Account accountTo = accountsRepository.getAccount(accountToId);

			// Validate accounts exist
			if (accountFrom == null || accountTo == null) {
				throw new IllegalArgumentException("One or both accounts do not exist.");
			}

			// Validate positive amount
			if (amount.compareTo(BigDecimal.ZERO) <= 0) {
				throw new IllegalArgumentException("Amount to transfer must be positive.");
			}

			// Validate sufficient balance
			if (accountFrom.getBalance().compareTo(amount) < 0) {
				throw new InsufficientBalanceException("Insufficient balance for transfer.");
			}
			
			// Check if accounts are the same
		    if (accountFromId.equals(accountToId)) {
		        throw new IllegalArgumentException("Cannot transfer money to the same account.");
		    }

			// Perform transfer
			accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
			accountTo.setBalance(accountTo.getBalance().add(amount));

			// Update balances in repository
			accountsRepository.updateBalance(accountFromId, accountFrom.getBalance());
			accountsRepository.updateBalance(accountToId, accountTo.getBalance());

			// Notify account holders
			final String transferDescription = "Transfer from " + accountFromId + " to " + accountToId + ": $" + amount;
			log.info("Transfer initiated from {} to {} with amount {}", accountFromId, accountToId, amount);
			notificationService.notifyAboutTransfer(accountFrom, transferDescription);
			notificationService.notifyAboutTransfer(accountTo, transferDescription);
		} finally {
			lock.unlock();
		}
	}

}