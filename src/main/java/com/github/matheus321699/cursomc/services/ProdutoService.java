package com.github.matheus321699.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.github.matheus321699.cursomc.domain.Categoria;
import com.github.matheus321699.cursomc.domain.Produto;
import com.github.matheus321699.cursomc.repositories.CategoriaRepository;
import com.github.matheus321699.cursomc.repositories.ProdutoRepository;
import com.github.matheus321699.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public List<Produto> findAll() {
		List<Produto> lista = repo.findAll();
		return lista;
	}
	
	/*
	 * Método para buscar por Id
	 */
	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));	
	}
	
	/*
	 * Criando um serviço de paginação para buscar registros no banco de dados de acordo
	 * com uma quantidade determinada de regsitros.
	 */
	// Page encapsula informações e operações de paginações
	/*
	 * Parâmetro page: qual a página
	 * Parâmetro linesPage: Linhas por página
	 * Parâmetro orderBy: por qual atributo será ordenado
	 * Parâmetro : qual direção deve ser ordenado, ascendente ou descendente
	 */
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPage, String orderBy, String direction){
		/* pageRequest: Objeto que vai preparar as informações para realizar uma consulta que 
		* retorne a página de dados
		*/
		PageRequest pageRequest = PageRequest.of(page, linesPage, Direction.valueOf(direction), orderBy); {
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.search(nome, categorias, pageRequest);
		}
	}
}