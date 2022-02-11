package com.github.matheus321699.cursomc.resources;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.matheus321699.cursomc.domain.Produto;
import com.github.matheus321699.cursomc.dto.ProdutoDTO;
import com.github.matheus321699.cursomc.resources.utils.URL;
import com.github.matheus321699.cursomc.services.ProdutoService;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService service; 
	
	
	/* Utilizando o ResponseEntity: Em situações que precisamos ter mais controle 
	 * sobre a resposta HTTP em um endpoint, o próprio Spring nos oferece a classe 
	 * ResponseEntity que nos permite manipular os dados HTTP da resposta.
	 */
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id) {
		Produto obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
	
	}
	/*
	 *  Handler: Objeto especial que intercepta a resposta HTTP caso ocorra uma exceção
	 *  e lança uma reposta HTTP adequada, no caso 404. 
	 */
	
	@RequestMapping(value = "/todos", method=RequestMethod.GET)
	public ResponseEntity<List<Produto>> findAll() {
		List<Produto> lista = service.findAll();
		
		return ResponseEntity.ok().body(lista);
	
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			/*
			 * @RequestParam: Anotação para deixar parâmetros opcionais
			 */
			@RequestParam(value="nome", defaultValue = "") String nome, 
			@RequestParam(value="categorias", defaultValue = "") String categorias, 
			@RequestParam(value="page", defaultValue = "0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue = "nome") String orderBy, 
			@RequestParam(value="direction", defaultValue = "ASC") String direction) {	
		String nomeDecode = URL.decodeParam(nome);
		List<Integer> ids = URL.decodeIntList(categorias);
		Page<Produto>	list = service.search(nomeDecode, ids, page, linesPerPage, orderBy, direction);
		Page<ProdutoDTO> listDTO = list.map(obj -> new ProdutoDTO(obj));
		return ResponseEntity.ok().body(listDTO);
	
	}
}
