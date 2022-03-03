package com.github.matheus321699.cursomc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/*
 * Classe de filtro de autorização baseado um uma verificação
 * de token de usuário.
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JWTUtil jwtUtil;
	
	private UserDetailsService userDetailsService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwUtil;
		
		/*
		 * userDetails service é utilizado para realizar a busca de usuário pelo email.
		 */
		this.userDetailsService = userDetailsService;
	}
	
	// Método padrão de filtro que intercepta a requisição e verifica se o usuário está autorizado.
	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain chain) throws IOException, ServletException {
		
		// Pegando valor do cabeçalho Autorization que vem na requisição e guardando em uma String.
		String header = request.getHeader("Authorization");
	
		// Testando header, startsWith: verificar se string inicia com determinados caracters
		if(header != null && header.startsWith("Bearer ")) {
			
			// subString: pegar valor da String sem os 7 primeiros caracters
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));	
		
			// Testando objeto construído auth.
			if(auth != null) {
				// Função para liberar o acesso ao usuário
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		
		// Dizendo para o filtro continuar a execução normal da requisição, após validação do token 
		chain.doFilter(request, response);
	}

	/*
	 * Método que recebe valor que vai estar na frente do Beares, o token, e vai retornar um objeto
	 * do tipo UsernamePasswordAuthenticationToken do Spring Security.
	 */
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.tokenValido(token)) {
			String username = jwtUtil.getUserName(token);
			UserDetails user = userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
	
}
