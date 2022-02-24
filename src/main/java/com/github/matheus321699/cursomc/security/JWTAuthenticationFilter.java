package com.github.matheus321699.cursomc.security;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.matheus321699.cursomc.dto.CredenciaisDTO;

/*
 * Classe com filtro de autenticação que intercepta a requisição de login, verifica as credenciais
 * e vai autenticar o usuário, gerando o token JWT em casos de autenticação. 
 * 
 * Obs: url com o endpoint /login já é reservado ao spring security.
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	
	private AuthenticationManager authenticationManager;
	
	private JWTUtil jwUtil;
	
	/*
	 *  Injetando  classe JWUtil e AuthenticationManager pelo construtor, quando for instanciar
	 *  a classe JWTAuthenticationFilter.
	 */
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwUtil) {
		this.authenticationManager = authenticationManager;
		this.jwUtil = jwUtil;
	}
	
	
	// Método que tenta autenticar, pegando dados que vem da requisição.
	public Authentication attemAuthentication(HttpServletRequest req,
											  HttpServletResponse res) throws AuthenticationException {
		try {
			
			// Instanciando objeto creds com dados que vieram da requisição.
			CredenciaisDTO creds = new ObjectMapper()
						.readValue(req.getInputStream(), CredenciaisDTO.class);
			
			// Passando valores da requisição para instanciar o objeto authToken ou token do spring security
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
			
			/*
			 * Chamando método authenticate do objeto authenticationManager passado pelo constrututor
			 * para verificar se o usuário e senha são válidos, através do que foi implementado nas classe
			 * UserSS criada que implementa o UserDetails e UserDetailsService.
			 */
			Authentication auth = authenticationManager.authenticate(authToken);
			
			/*
			 *  Retornando objeto que informa para spring security se autenticação ocorreu com sucesso, se 
			 *  o usuário e senha estiverem correto ele vai passar se não retorna um badcredencials.
			 */
			return auth;
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 *  Método para realizar um procedimento caso a autenticação ocorra com sucesso, gerando um token 
	 *  e adicionando na resposta da requisição.
	 */
	protected void sucessfulAuthentication(HttpServletRequest req,
											  HttpServletResponse res,
											  FilterChain chain,
											  Authentication auth) throws IOException, ServerException {
	
	/*
	 * Utilizando o método getPrincipal para retornar um usuáio do spring security, fazendo um 
	 * casting para o UserSS pegando o email do usuário.
	 */
	String username = ((UserSS) auth.getPrincipal()).getUsername();
	
	// Passando email para gerar e retornar token 
	String token = jwUtil.generateToken(username);
	
	/*
	 * Retornando token de resposta na requisição, acrescentando ele como
	 * cabeçalho  da resposta, com parâmetro de nome de cabeçalho Authorization, com valor
	 * Bearer concatenado com o token.
	 */
	res.addHeader("Authorization", "Bearer" + token);
	}
	
}
