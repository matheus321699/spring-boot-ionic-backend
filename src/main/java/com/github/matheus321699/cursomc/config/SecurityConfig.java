package com.github.matheus321699.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.github.matheus321699.cursomc.security.JWTAuthenticationFilter;
import com.github.matheus321699.cursomc.security.JWTAuthorizationFilter;
import com.github.matheus321699.cursomc.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Anotação para autotizar endpoints para perfis específicos
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	// Injetando interface UserDetailsService, o spring se encarrega
	// de encontrar uma implementação da interface.
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	/*
	 *  Definindo vetor que possui quais caminhos estarão liberados
	 *  para acesso as URL's
	 */
	private static final String[] PUBLIC_MATCHERS = {
			// Todo mundo que vim após desse caminho vai estar liberado
			"/h2-console/**"
	};
	
	/*
	 * Vetor com caminho somente de leitura ou de recuperação de dados, 
	 * para que o usuário não possa alterar os caminhos.
	 */
	private static final String[] PUBLIC_MATCHERS_GET = {
			// Todo mundo que vim após desse caminho vai estar liberado
			"/produtos/**",
			"/categorias/**",
			"/clientes/**", 
			"/pedidos/**"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/*
		 *  Configuração específica do banco de dados H2, para pegar profiles
		 *  ativos, com a condição de que caso esteja no profile test, significa
		 *  que eu vou querer acessar o H2.
		 */
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		// Acessando objeto http para que todos os caminhos que estiverm
		// no vetor PUBLIC_MATCHERS eu vou permitir.
		http.cors().and().csrf().disable();
		http.authorizeRequests()
		
		// Permitindo somente o método HTTP GET nos caminhos no vetor PUBLIC_MATCHERS_GET
		.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
		.antMatchers(PUBLIC_MATCHERS).permitAll()
		.anyRequest().authenticated();
		
		// Registrando filtro de autenticação
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		
		// Registrando filtro de autorização
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));

		/*
		 * Configuração para assegurar que o nosso back end não
		 * vai criar sessão de usuário.
		 */
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	/*
	 * Méotdo que defini um Bean de CorsConfigurationSource para permitir acessos de
	 * múltiplas fontes para todos os caminhos, ou seja, 
	 * 
	 * um método para permitir acesso aos meus endpoints por múltiplas fontes
	 * com as configurações básicas.
	 */
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		// Liberando configurações básicas para teste
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return (CorsConfigurationSource) source;
	}
	
	/*
	 * Método configure sobrescrito com sobrecarga para dizer quem é o UserDetails que estamos 
	 * utilizando e quem é o algoritmo de retorno de codificação da senha, que no
	 * caso é o bCryptPasswordEncoder().
	 */
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	@Bean
	// Utilizado para armazenar senha em forma de código no banco de dados
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
