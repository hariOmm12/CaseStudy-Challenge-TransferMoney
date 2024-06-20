package com.dws.challenge.repository;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;

@Repository
public interface AccountsRepository {

	void createAccount(Account account) throws DuplicateAccountIdException;

	Account getAccount(String accountId);

	void clearAccounts();

	/**
	 * Update the balance of the specified account by the given amount.
	 */
	void updateBalance(String accountId, BigDecimal amount);
}
