package com.github.matheus321699.cursomc.domain.enums;

public enum TipoCliente {

	/*
	 * O Banco de dados pode salvar os valores do enum
	 * em forma de string ou numérica, por exemplo, pessoa
	 * física equivale a 0 e pessoa jurídica equivale a 1.
	 */
	
	/*
	 * Por padrão os valores do enum são salvos em forma 
	 * númerica no banco, contudo, é possível fazer um controle manual
	 * dos números que serão correspondentes aos valores do enum, inserindo
	 * esses valores entre parêntese. Por ex: PESSOAFISICA(1, "Pessoa"). Támbem
	 * deve ser criado um construtor.
	 */
	PESSOAFISICA(1, "Pessoa Física"), 
	PESSOAJURIDICA(2, "Pessoa Jurídica");
	
	private int cod;
	private String descricao;
	
	private TipoCliente(int cod, String descrição) {
		this.cod = cod;
		this.descricao = descrição;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDecricao() {
		return descricao;
	}
	
	public static TipoCliente toEnum(Integer cod) {
		if(cod == null) {
			return null;
		}
		
		for(TipoCliente x : TipoCliente.values()) {
			if(cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
	
}
