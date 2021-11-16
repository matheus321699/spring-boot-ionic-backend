package com.github.matheus321699.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.github.matheus321699.cursomc.domain.Produto;


/* Interface JPARepository. Você já chegou a fazer algum repositório (ou DAO) 
 * genérico com os métodos buscar , salvar (ou atualizar ) e remover ? Pois é, 
 * essa interface é mais ou menos isso. Ela tem todos os métodos que a gente 
 * precisa para fazer um CRUD (criar, ler, atualizar, deletar).
*/
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer>{

}
