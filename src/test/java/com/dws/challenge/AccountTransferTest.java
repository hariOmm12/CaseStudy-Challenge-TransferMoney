package com.dws.challenge;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.InsufficientBalanceException;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.NotificationService;


/**
 *  Test cases to cover Transfer Functionality
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountTransferTest { 

	private AccountsService accountsService;
	private AccountsRepository accountsRepository;
	private NotificationService notificationService;

	@BeforeEach
	void setUp() {
		accountsRepository = mock(AccountsRepository.class);
		notificationService = mock(NotificationService.class);
		accountsService = new AccountsService(accountsRepository, notificationService);
	}

	@Test
	void transferMoney_Successful() {
		final Account accountFrom = new Account("1", new BigDecimal("100.00"));
		final Account accountTo = new Account("2", new BigDecimal("50.00"));

		when(accountsRepository.getAccount("1")).thenReturn(accountFrom);
		when(accountsRepository.getAccount("2")).thenReturn(accountTo);

		accountsService.transferMoney("1", "2", new BigDecimal("30.00"));

		assertEquals(new BigDecimal("70.00"), accountFrom.getBalance());
		assertEquals(new BigDecimal("80.00"), accountTo.getBalance());
		verify(notificationService).notifyAboutTransfer(accountFrom, "Transfer from 1 to 2: $30.00");
		verify(notificationService).notifyAboutTransfer(accountTo, "Transfer from 1 to 2: $30.00");
	}

	@Test
	void transferMoney_InsufficientBalance() {
		final Account accountFrom = new Account("1", new BigDecimal("100.00"));
		final Account accountTo = new Account("2", new BigDecimal("50.00"));

		when(accountsRepository.getAccount("1")).thenReturn(accountFrom);
		when(accountsRepository.getAccount("2")).thenReturn(accountTo);

		assertThrows(InsufficientBalanceException.class, () -> 
		accountsService.transferMoney("1", "2", new BigDecimal("150.00"))
				);

		assertEquals(new BigDecimal("100.00"), accountFrom.getBalance());
		assertEquals(new BigDecimal("50.00"), accountTo.getBalance());
		verify(notificationService, never()).notifyAboutTransfer(any(), anyString());
	}

	@Test
	void testTransferMoney_AccountNotFound() {
		// Mock repository behavior for account not found
		when(accountsRepository.getAccount("fromAccountId")).thenReturn(null);
		when(accountsRepository.getAccount("toAccountId")).thenReturn(new Account("toAccountId"));

		// Perform transfer with account not found
		assertThrows(IllegalArgumentException.class, () -> {
			accountsService.transferMoney("fromAccountId", "toAccountId", BigDecimal.valueOf(200));
		});

		// Verify no notification sent
		verify(notificationService, never()).notifyAboutTransfer(any(Account.class), anyString());
	}

}


