package com.github.matheus321699.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.matheus321699.cursomc.services.DBService;

/*
 * Classe de configuração: é uma classe que vai ter algum método ou
 * alguma informação que vai estar disponível no sistema e vai ser 
 * configurado no início da execução da aplicação. 
 */
@Configuration
@Profile("dev")
/*
 * Arquivo de configuração específico para o application-test.properties,
 * para que todos os beans que tiverem dentro dessa classe irão ser ativados
 * apenas quando o profile de test estiver ativo no application.properties.
 */
public class DevConfig {

	@Autowired
	private DBService dbService;
	
	// Pega o valor da chave e armazena na string strategy
	@Value("$[spring.jpa.hibernate.ddl-auto]")
	private String strategy;
	
	@Bean
	/*
	 * Método responsável por instanciar o meu banco de dados no
	 * profile de teste.
	 */
	public boolean instanciateDatabase() throws ParseException {
		/*
		 * Pegando valor da chave spring.jpa.hibernate.ddl-auto para intanciar 
		 * base de dados somente se o valor da chave spring.jpa.hibernate.ddl-auto 
		 * for igual a create.
		 */
		if(!"create".equals(strategy)) {
			return false;
		}
		
		dbService.instantiateTestDatabase();
		return true;
	}
}
