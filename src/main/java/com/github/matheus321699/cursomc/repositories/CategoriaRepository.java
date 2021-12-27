package com.github.matheus321699.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.matheus321699.cursomc.domain.Categoria;

/*
 * JpaRepository: Interface responsável por criar operações de CRUD
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{

}
