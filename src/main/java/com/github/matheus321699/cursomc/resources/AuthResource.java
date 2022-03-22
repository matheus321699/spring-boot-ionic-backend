package com.github.matheus321699.cursomc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.matheus321699.cursomc.dto.EmailDTO;
import com.github.matheus321699.cursomc.security.JWTUtil;
import com.github.matheus321699.cursomc.security.UserSS;
import com.github.matheus321699.cursomc.services.AuthService;
import com.github.matheus321699.cursomc.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService service;

	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		
		// Pegando usuário logado
		UserSS user = UserService.authenticated();
		
		// Gerando um novo token com o meu usuário, com a data atual e tempo de expiração renovado
		String token = jwtUtil.generateToken(user.getUsername());
		
		// Adicionando token na resposta da minha requisição
		response.addHeader("Authorization", "Bearer " + token);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	// Método que recebe email do usuário na forma de Request Body
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDTO) {
		service.sendNewPassword(objDTO.getEmail());
		return ResponseEntity.noContent().build();
	}

}
