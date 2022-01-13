package com.github.matheus321699.cursomc.dto;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.github.matheus321699.cursomc.domain.Cliente;

public class ClienteDTO  {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	/* 
	 * Valições customizadas com o framework 
	 */
	@NotEmpty(message="Preenchimento obrigatório!")
	@Length(min=5, max=80, message="O tamanho deve ser entre 5 e 120 caracteres")
	private String nome;
	
	@NotEmpty
	@Length
	private String email;
	
	public ClienteDTO() {
		
	}
	
	public ClienteDTO(Cliente obj) {
		id = obj.getId();
		nome = obj.getNome();
		email = obj.getEmail();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}
