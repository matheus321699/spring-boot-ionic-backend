package com.github.matheus321699.cursomc.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.matheus321699.cursomc.domain.enums.Perfil;

/*
 * Classe User Spring Security que implementa o contrato UserDetails
 * para trabalhar com usuários.
 */
public class UserSS implements UserDetails{
	private static final long serialVersionUID = 1L;
	
	// Atributos de usuário
	private Integer id;
	private String email;
	private String senha;
	
	/*
	 *  Lista interna de tipo de usuários que o Spring Security exige.
	 *  Ou lista de usuários autorizados.
	 */
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserSS() {
		
	}
	
	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		
		/*
		 * Convertendo lista de perfis de usuários para Lista interna de tipo de usuários 
		 * que o Spring Security exige.
		 */
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDecricao())).collect(Collectors.toList());
	}



	public Integer getId() {
		return id;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	/*
	 * Método para definir tempo de expiração de uauário;
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	// Método para testar se o usuário possui o perfil pedido
	public boolean hasRole(Perfil perfil) {
		
		// Testa se o usuário possui o peerfil passado em forma de String
		return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getDecricao()));
	}

	
	
}
