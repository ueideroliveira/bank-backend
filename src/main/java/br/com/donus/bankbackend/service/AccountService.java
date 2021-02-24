package br.com.donus.bankbackend.service;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import br.com.donus.bankbackend.dto.AccountDTO;
import br.com.donus.bankbackend.dto.DepositDTO;
import br.com.donus.bankbackend.dto.TransferDTO;
import br.com.donus.bankbackend.exceptions.AccountNotFoundException;
import br.com.donus.bankbackend.exceptions.PersonAccountFoundException;
import br.com.donus.bankbackend.exceptions.TransferBalanceNegativeException;
import br.com.donus.bankbackend.model.Account;
import br.com.donus.bankbackend.repository.AccountRepository;
import br.com.donus.bankbackend.util.Constants;

@Service
public class AccountService {
	
	@Autowired
	private MessageSource message;

	@Autowired
	private AccountRepository accountRepository;
	
	private Locale locale = LocaleContextHolder.getLocale();
	
	public Account findById(Integer id) {
		return accountRepository.findById(id).orElseThrow(accountNotFoundException());
	}
	
	public Account create(AccountDTO account) {
		accountRepository.findByDocument(account.getDocument())
			.ifPresent(a -> {
				throw new PersonAccountFoundException(message.getMessage("message.person.account.found", null, locale));
			});
		
		Account accountEntity = createNewAccount(account);
		accountRepository.save(accountEntity);
		return accountEntity;
	}

	public Account deposit(DepositDTO deposit) {
		Account account = accountRepository.findById(deposit.getAccountId())
					.orElseThrow(accountNotFoundException()
					);
		return updateBalance(account, sumBalance(account, deposit.getValue()));
	}

	public void transfer(TransferDTO transfer) {
		Optional<Account> originAccountOptional = accountRepository.findById(transfer.getOriginAccountId());
		Account originAccount = originAccountOptional.orElseThrow(accountNotFoundException());
		
		Optional<Account> destinyAccountOptional = accountRepository.findById(transfer.getDestinyAccountId());
		Account destinyAccount = destinyAccountOptional.orElseThrow(accountNotFoundException());
		
		Double originAccountBalance = minusBalance(originAccount, transfer.getValue());
		validBalanceNegative(originAccountBalance);
		
		Double destinyAccountBalance = sumBalance(destinyAccount, transfer.getValue());
		
		updateBalance(originAccount, originAccountBalance);
		updateBalance(destinyAccount, destinyAccountBalance);
	}

	private Supplier<? extends AccountNotFoundException> accountNotFoundException() {
		return () -> new AccountNotFoundException(message.getMessage("message.account.not.found", null, locale));
	}

	private void validBalanceNegative(Double originAccountBalance) {
		if(originAccountBalance < 0) {
			throw new TransferBalanceNegativeException(message.getMessage("message.transfer.balance.negative", null, locale));
		}
	}
	
	private Account updateBalance(Account account, Double value) {
		account.setBalance(value);
		accountRepository.save(account);
		return account;
	}
	
	private Double sumBalance(Account account, Double value){
		return account.getBalance() + value;
	}
	
	private Double minusBalance(Account account, Double value){
		return account.getBalance() - value;
	}

	private Account createNewAccount(AccountDTO accountDTO) {
		Account account = new Account();
		account.setDocument(accountDTO.getDocument());
		account.setName(accountDTO.getName());
		account.setBalance(Constants.BALANCE_INITIAL);
		return account;
	}
	
}
