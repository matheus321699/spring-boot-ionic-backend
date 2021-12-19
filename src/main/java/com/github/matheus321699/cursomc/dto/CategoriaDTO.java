package com.github.matheus321699.cursomc.dto;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.github.matheus321699.cursomc.domain.Categoria;

/*
 * DTO(Data Tranfer Object) ou Objeto de Transferência de Dados: É um objeto que possui
 * somente os dados solicitados para alguma operação no sistema.
 */

// Criando um DTO de Categoria
public class CategoriaDTO {
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	@NotEmpty(message = "Preenchimento obrigatório")
	@Length(min=5, max=80, message="O tamanho deve ser entre 5 e 80 caracteres")
	private String nome;
	
	public CategoriaDTO() {	
	}
	
	/*
	 * Covertendo Lista de Categorias para uma lista de 
	 * CategoriaDTO através de um construtor que recebe
	 * o objeto correspondente das entidades de domínio.
	 */
	public CategoriaDTO(Categoria obj) {
		id = obj.getId();
		nome = obj.getNome();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
	
}
