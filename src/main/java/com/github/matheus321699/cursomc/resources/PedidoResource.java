package com.github.matheus321699.cursomc.resources;


import java.net.URI;


import javax.security.sasl.AuthenticationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.matheus321699.cursomc.domain.Pedido;
import com.github.matheus321699.cursomc.services.PedidoService;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResource {

	@Autowired
	private PedidoService service; 
	
	
	/* Utilizando o ResponseEntity: Em situações que precisamos ter mais controle 
	 * sobre a resposta HTTP em um endpoint, o próprio Spring nos oferece a classe 
	 * ResponseEntity que nos permite manipular os dados HTTP da resposta.
	 */
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	public ResponseEntity<Pedido> find(@PathVariable Integer id) {
		Pedido obj = service.find(id);
		
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
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj) {
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

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<Pedido>> findPage(
			/*
			 * @RequestParam: Anotação para deixar parâmetros opcionais
			 */
			@RequestParam(value="page", defaultValue = "0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue = "24") Integer linesPage, 
			@RequestParam(value="orderBy", defaultValue = "instante") String orderBy, 
			@RequestParam(value="direction", defaultValue = "DESC") String direction) throws AuthenticationException {	
		Page<Pedido> list = service.findPage(page, linesPage, orderBy, direction);
		return ResponseEntity.ok().body(list);
	
	}
	
}
