package com.github.matheus321699.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.github.matheus321699.cursomc.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

	/*
	 * Criando operação ou método para realizar uma busca por email, 
	 * utilizando recurso do Spring Data chamado padrão de nomes, ou 
	 * seja ao cria um método com o nome findByEmail o Spring Data 
	 * implementa automaticamente esse método, buscando um cliente
	 * no banco de dados pelo email.
	 */
	/*
	 * Atribuito readOnly: anotação para que o método não necessite 
	 * ser envolvido como uma transação de banco de dados, fazendo
	 * com que fique mais rápido e diminui o locking do gerenciamento
	 * de transações do banco de dados.
	 */
	@Transactional(readOnly = true)
	Cliente findByEmail(String email);
	
}
