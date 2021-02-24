package br.com.donus.bankbackend.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class DepositDTO {

	@NotNull
	private Integer accountId;
	
	@NotNull
	@Max(value = 2000)
	@Positive
	private Double value;
}
