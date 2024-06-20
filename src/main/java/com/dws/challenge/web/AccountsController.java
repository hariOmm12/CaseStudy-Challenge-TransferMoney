package com.dws.challenge.web;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.TransferRequest;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.service.AccountsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
@Validated
public class AccountsController {

	private final AccountsService accountsService;

	@Autowired
	public AccountsController(AccountsService accountsService) {
		this.accountsService = accountsService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
		log.info("Creating account {}", account);

		// Check if accountId is null or empty
		if (StringUtils.isEmpty(account.getAccountId())) {
			return new ResponseEntity<>("Account ID must not be empty", HttpStatus.BAD_REQUEST);
		}

		try {
			this.accountsService.createAccount(account);
		} catch (final DuplicateAccountIdException daie) {
			throw daie; // Let @ControllerAdvice handle this exception
		} catch (final ConstraintViolationException cve) {
			return new ResponseEntity<>(cve.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping(path = "/{accountId}")
	public Account getAccount(@PathVariable String accountId) {
		log.info("Retrieving account for id {}", accountId);
		return this.accountsService.getAccount(accountId);
	}

	@PostMapping("/transfer")
	public ResponseEntity<String> transferMoney(@RequestBody TransferRequest request) { 
		accountsService.transferMoney(request.getAccountFromId(), request.getAccountToId(), request.getAmount());
		log.info("Transfer done from {} to {} with amount {}", request.getAccountFromId(), request.getAccountToId(), request.getAmount());
		return ResponseEntity.ok("Transfer successful");

	}

}
