package com.dws.challenge.repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

	private final Map<String, Account> accounts = new ConcurrentHashMap<>();

	@Override
	public  void createAccount(Account account) throws DuplicateAccountIdException {
		final Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
		if (previousAccount != null) {
			throw new DuplicateAccountIdException(
					"Account id " + account.getAccountId() + " already exists!");
		}
	}

	@Override
	public  Account getAccount(String accountId) {
		return accounts.get(accountId);
	}

	@Override
	public  void clearAccounts() {
		accounts.clear();
	}

	@Override
	public  void updateBalance(String accountId, BigDecimal amount) {
		final Account account = accounts.get(accountId);
		if (account != null) {
			account.setBalance(amount);
			accounts.put(accountId, account);
		} else {
			throw new IllegalArgumentException("Account not found for accountId: " + accountId);
		}
	}

}
