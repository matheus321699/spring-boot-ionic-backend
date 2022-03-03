package com.github.matheus321699.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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
	
	// Método para verificar se o token é válido
	public boolean tokenValido (String token) {
		
		// O Claims armazena as reivindicações do token, no caso o usuário e tempo do token
		Claims claims = getClaims(token);
		
		// Testando objeto claims
		if(claims != null) {
			
			// Obtendo username a partir do claims.
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
		
			// Testando se o token está expirado
			if(username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}
		return false;
	}
	
	// Método para pegar usuário a partir do token.
	public String getUserName(String token) {
		
		// O Claims armazena as reivindicações do token, no caso o usuário e tempo do token
		Claims claims = getClaims(token);
		
		// Testando objeto claims
		if(claims != null) {
			
			// Obtendo username a partir do claims.
			return claims.getSubject();
		}
		
		return null;
	}

	// Método para pegar reivindicações do token.
	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} 
		
		catch (Exception e) {
			return null;
		}
	}
	
	
}
