 package com.github.matheus321699.cursomc.domain.enums;

public enum Perfil {

	/*
	 * Segundo parâmetro corresponde é a palavra que corresponde o enum.
	 * O ROLE_ é exigência para tipo de usuário do spring security.
	 */
	ADMIN(1, "ROLE_ADMIN"),
	CLIENTE(2, "ROLE_CLIENTE");
	
	// Valor do código de ADMIN e CLIENTE
	private int cod;
	
	// Descrição do enum ADMIN e CLIENTE
	private String descricao;
	
	
	private Perfil(int cod, String descrição) {
		this.cod = cod;
		this.descricao = descrição;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDecricao() {
		return descricao;
	}
	
	public static Perfil toEnum(Integer cod) {
		if(cod == null) {
			return null;
		}
		
		for(Perfil x : Perfil.values()) {
			if(cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
}
