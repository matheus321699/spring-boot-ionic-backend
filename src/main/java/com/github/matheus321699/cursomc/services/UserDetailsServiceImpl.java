package com.github.matheus321699.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.matheus321699.cursomc.domain.Cliente;
import com.github.matheus321699.cursomc.repositories.ClienteRepository;
import com.github.matheus321699.cursomc.security.UserSS;


/*
 * Classe que implementa interface UserDetailsService do Spring Security, 
 * a qual permite a busca pelo nome do usuário.
 * 
 *  Anotação @Service: utilizada para que esse serviço seja um componente 
 *  do framework que possa ser injetado.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private ClienteRepository repo;

	@Override
	// Método que recebe usuário e retorna o UserDetails
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		/* 
		 * Implementando busca por email com método findByEmail
		 * do ClienteRepository.
		 */
		Cliente cli = repo.findByEmail(email);
		if(cli == null) {
			// Exceção do Spring security;
			throw new UsernameNotFoundException(email);
		}
		
		return new UserSS(cli.getId(), cli.getEmail(), cli.getSenha(), cli.getPerfis());
	}
	

}
