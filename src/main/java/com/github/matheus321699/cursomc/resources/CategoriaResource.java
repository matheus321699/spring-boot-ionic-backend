package com.github.matheus321699.cursomc.resources;


import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.matheus321699.cursomc.domain.Categoria;
import com.github.matheus321699.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service; 
	
	
	/* Utilizando o ResponseEntity: Em situações que precisamos ter mais controle 
	 * sobre a resposta HTTP em um endpoint, o próprio Spring nos oferece a classe 
	 * ResponseEntity que nos permite manipular os dados HTTP da resposta.
	 */
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		Categoria obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
	
	}
	/*
	 *  Handler: Objeto especial que intercepta a resposta HTTP caso ocorra uma exceção
	 *  e lança uma reposta HTTP adequada, no caso 404. 
	 */
	
	/*
	 * Resposta HTTP sem corpo
	*/
	@RequestMapping(method = RequestMethod.POST)
	/* 
	 * @RequestBody: Anotação para que o objeto categoria seja construido a partir dos dados json
	 * enviados.
	*/
	public ResponseEntity<Void> insert(@RequestBody Categoria obj) {
		/*
		 * O protocolo HTTP quando há uma inserção de um novo recurso, 
		 * ele possui um código de reposta especial para isso. Para verificar
		 * códigos de resposta HTTP basta digitar no goolgle http status code. No 
		 * caso de inserção de um novo recurso o HTTP possui um código especial
		 * 10.2.201 Create e retorna u URI do recurso adicionado.
		 */
		obj = service.insert(obj);
		/*
		 * Chamada que pega a URI do novo recurso que foi inserido
		 */
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody Categoria obj, @PathVariable Integer id) {
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
		
	}
}
