package com.github.matheus321699.cursomc.resources;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.matheus321699.cursomc.domain.Cliente;
import com.github.matheus321699.cursomc.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService service; 
	
	
	/* Utilizando o ResponseEntity: Em situações que precisamos ter mais controle 
	 * sobre a resposta HTTP em um endpoint, o próprio Spring nos oferece a classe 
	 * ResponseEntity que nos permite manipular os dados HTTP da resposta.
	 */
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer id) {
		Cliente obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
	
	}
	/*
	 *  Handler: Objeto especial que intercepta a resposta HTTP caso ocorra uma exceção
	 *  e lança uma reposta HTTP adequada, no caso 404. 
	 */
	
}
