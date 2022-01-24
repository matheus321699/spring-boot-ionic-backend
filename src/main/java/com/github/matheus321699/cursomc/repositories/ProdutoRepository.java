package com.github.matheus321699.cursomc.repositories;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.github.matheus321699.cursomc.domain.Categoria;
import com.github.matheus321699.cursomc.domain.Produto;


/* Interface JPARepository. Você já chegou a fazer algum repositório (ou DAO) 
 * genérico com os métodos buscar , salvar (ou atualizar ) e remover ? Pois é, 
 * essa interface é mais ou menos isso. Ela tem todos os métodos que a gente 
 * precisa para fazer um CRUD (criar, ler, atualizar, deletar).
*/
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer>{

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	/*
	 * Anotação @Param("nome"): anotação para pegar parâmetro passado e enviar para o nome 
	 * da consulta JPQL.
	 */
	Page<Produto> search(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable pageRequest);

	/*
	 * Criando um método com padrão nomes do spring data que implementam um método automaticamente, não sendo necessários passar
	 * a consulta JPQL:
	 * 
	 * 	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
	 */

}
