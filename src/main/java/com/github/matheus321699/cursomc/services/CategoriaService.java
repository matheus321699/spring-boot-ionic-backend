package com.github.matheus321699.cursomc.services;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.matheus321699.cursomc.domain.Categoria;
import com.github.matheus321699.cursomc.repositories.CategoriaRepository;
import com.github.matheus321699.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;
	
	/*
	 * Método para buscar por Id
	 */
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));	
	}
	
	/*
	 * Método isert para inserir objeto no banco de dados 
	 */
	public Categoria insert(Categoria obj) {
		// Setar Id para garantir que o objeto inserido é um novo registro
		obj.setId(null);
		return repo.save(obj);
	}
	
	/*
	 * Método para atualizar dados no banco de dados
	 */
	public Categoria update(Categoria obj) {
		// Verificando se o objeto existe com o método fin
		find(obj.getId());
		return repo.save(obj);
	}
	
}