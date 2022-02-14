package com.github.matheus321699.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.github.matheus321699.cursomc.domain.Cliente;
import com.github.matheus321699.cursomc.dto.ClienteDTO;
import com.github.matheus321699.cursomc.repositories.ClienteRepository;
import com.github.matheus321699.cursomc.resources.exception.FieldMessage;

/*
 * Criando Validator personalizado para a anotação ClienteInsert
 * e para o ClienteNewDTO para validar email.
 */
public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	/*
	 * Objeto que pega o Id da URI dentro do Validator, onde o mesmo possui uma 
	 * função que permite obter o parâmetro da URI.
	 */
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		
		/*
		 * Pegando Map de atributos que vem na requisição
		 */
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriID = Integer.parseInt(map.get("id"));
		
		List<FieldMessage> list = new ArrayList<>();
		
		/*
		 * Verificando email existente
		 */
		Cliente aux = repo.findByEmail(objDto.getEmail());
		
		/*
		 * Teste para validar email existente e que já está cadastrado
		 * em outro usuário.
		 */
		if (aux != null && !aux.getId().equals(uriID)) {
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}