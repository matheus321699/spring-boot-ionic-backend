package com.github.matheus321699.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.github.matheus321699.cursomc.security.UserSS;

public class UserService {
	
	// Retorna usuário autenticado no sistema
	public static UserSS authenticated() {
		
		try {
			// Método para retornar usuário logado no sistema
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch (Exception e) {
			return null;
		}
	}

}
