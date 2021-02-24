package br.com.donus.bankbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.donus.bankbackend.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{

	Optional<Account> findByDocument(String document);
}
