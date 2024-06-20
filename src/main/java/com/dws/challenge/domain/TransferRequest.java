package com.dws.challenge.domain;

import lombok.Data;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Pojo for Transfer
 */
@Data
public class TransferRequest {
	@NotNull
	private String accountFromId;

	@NotNull
	private String accountToId;

	@NotNull
	@Min(value = 1, message = "Transfer amount must be positive.")
	private BigDecimal amount;
}
