package com.github.matheus321699.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.matheus321699.cursomc.domain.Cliente;
import com.github.matheus321699.cursomc.repositories.ClienteRepository;
import com.github.matheus321699.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	// Injeta senha no banco de dados
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;
	
	// Classe que gera valores aleatórios
	private Random rand = new Random();
	
	public void sendNewPassword(String email) {
		
		// Verificando se o email do cliente existe
		Cliente cliente = clienteRepository.findByEmail(email);
		
		// Testando cliente
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado!");
		}
		
		// Gerando nova senha para o usuário
		String newPass = newPassword();
		cliente.setSenha(pe.encode(newPass));
		
		clienteRepository.save(cliente);
		
		// Enviando email com a nova senha para o usuário
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	// Método auxiliar que gera uma nova senha
	private String newPassword() {
		
		// Gerando senha de 10 caracteres podendo ser digitos ou letras
		char[] vet = new char[10];
		for(int i=0; i<10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	// Função que gera senha aleatório com caracteres ou digitos 
	private char randomChar() {
		
		// Gera um número inteiro de 0 até 2, para servir como valor de opção de tipo de senha
		int opt = rand.nextInt(3);
		
		// Verificar na tabela code da biblioteca Random.
		
		if(opt == 0) { // gera um dígito
			return (char) (rand.nextInt(10) + 40);
		} 
		else if (opt == 1) { // gera letra maiuscula
			return (char) (rand.nextInt(26) + 65);
		}
		else { // gera letra minúscula
			return (char) (rand.nextInt(26) + 97);
		}
	}

}
