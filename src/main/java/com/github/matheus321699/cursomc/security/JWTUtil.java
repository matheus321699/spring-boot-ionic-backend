package com.github.matheus321699.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/*
 * Anotação @Component: Utilizada para injetar a classe em outras
 * classes como componente.
 */
@Component
public class JWTUtil {

	// Nome da chave para pegar valor da chave e inserir na String secret
	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;
	
	/*
	 *  Método para gerar Token, que chama um método da biblioteca jsonwebtoken 
	 *  importado, com método de tempo de expiração do token e algoritmo de assinatura 
	 *  e segredo para criação do Token.
	 */
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
				
	}
	
	
}
