package com.github.matheus321699.cursomc.resources;


import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.matheus321699.cursomc.domain.Categoria;
import com.github.matheus321699.cursomc.dto.CategoriaDTO;
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
	@PreAuthorize("hasAnyRole('ADMIN')") // Anotação para liberar acesso ao enpoint com usuário ADMIN
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDTO) {
		/*
		 * O protocolo HTTP quando há uma inserção de um novo recurso, 
		 * ele possui um código de reposta especial para isso. Para verificar
		 * códigos de resposta HTTP basta digitar no goolgle http status code. No 
		 * caso de inserção de um novo recurso o HTTP possui um código especial
		 * 10.2.201 Create e retorna u URI do recurso adicionado.
		 */
		Categoria obj = service.fromDTO(objDTO);
		obj = service.insert(obj);
		/*
		 * Chamada que pega a URI do novo recurso que foi inserido
		 */
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	
	/*
	 * O método de requisição HTTP PUT cria um novo recurso ou subsititui uma representação do recurso de 
	 * destino com os novos dados.
	 */
	@RequestMapping(value = "/{id}", method=RequestMethod.PUT)
	/*
	 * Em resumo, o @PathVariable é utilizado quando o valor da variável é passada diretamente na URL, 
	 * mas não como um parametro que você passa após o sinal de interrogação (?) mas sim quando o valor 
	 * faz parte da url.
	 * 
	 * A anotação @RequestBody indica que o valor do objeto virá do corpo da requisição;
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDTO, @PathVariable Integer id) {
		Categoria obj = service.fromDTO(objDTO);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
		
	}
	
	// endpoint que retorna todas as categorias
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		/*
		 * Covertendo Lista de Categorias para uma lista de 
		 * CategoriaDTO.
		 */
		
		List<Categoria>	list = service.findAll();
		/*
		 * Utilizando lista de Categoria para percorrer elementos
		 * e intanciar o DTO correspondente a cada elemento.
		 */
		List<CategoriaDTO> listDTO = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	
	}
	
	@RequestMapping(value="/page", method = RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			/*
			 * @RequestParam: Anotação para deixar parâmetros opcionais
			 */
			@RequestParam(value="page", defaultValue = "0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue = "24") Integer linesPage, 
			@RequestParam(value="orderBy", defaultValue = "nome") String orderBy, 
			@RequestParam(value="direction", defaultValue = "ASC") String direction) {	
		Page<Categoria>	list = service.findPage(page, linesPage, orderBy, direction);
		Page<CategoriaDTO> listDTO = list.map(obj -> new CategoriaDTO(obj));
		return ResponseEntity.ok().body(listDTO);
	
	}
}
