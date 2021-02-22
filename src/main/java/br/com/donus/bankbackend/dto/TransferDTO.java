package br.com.donus.bankbackend.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class TransferDTO {

	@NotNull
	private Long originAccountId;
	@NotNull
	private Long destinyAccountId;
	@Positive
	private Double value;
}
