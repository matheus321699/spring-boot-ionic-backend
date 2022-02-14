package com.github.matheus321699.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private Environment env;
	
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
			"/clientes/**"
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
	
	@Bean
	// Utilizado para armazenar senha em forma de código no banco de dados
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
		
}
