package br.com.donus.bankbackend.util.model;

import br.com.donus.bankbackend.dto.AccountDTO;
import br.com.donus.bankbackend.dto.DepositDTO;
import br.com.donus.bankbackend.model.Account;

public class AccountModel {

	public static AccountDTO getAccountDTOSaveController() {
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setDocument("26366469067");
		accountDTO.setName("Maria da Silva Santos");
		return accountDTO;
	}
	
	public static Account getAccount() {
		Account account = new Account();
		account.setId(1);
		account.setBalance(0.0);
		account.setDocument("26366469067");
		account.setName("Maria da Silva Santos");
		return account;
	}
	
	public static DepositDTO getDepositDTO() {
		DepositDTO depositDTO = new DepositDTO();
		depositDTO.setAccountId(1);
		depositDTO.setValue(250.0);
		return depositDTO;
	}
	
	public static Account getOriginAccount() {
		Account account = new Account();
		account.setId(2);
		account.setBalance(250.0);
		account.setDocument("26366469068");
		account.setName("João dos Santos");
		return account;
	}
}
