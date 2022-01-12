package com.github.matheus321699.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.matheus321699.cursomc.domain.Cidade;
import com.github.matheus321699.cursomc.domain.Cliente;
import com.github.matheus321699.cursomc.domain.Endereco;
import com.github.matheus321699.cursomc.domain.enums.TipoCliente;
import com.github.matheus321699.cursomc.dto.ClienteDTO;
import com.github.matheus321699.cursomc.dto.ClienteNewDTO;
import com.github.matheus321699.cursomc.repositories.ClienteRepository;
import com.github.matheus321699.cursomc.repositories.EnderecoRepository;
import com.github.matheus321699.cursomc.services.exceptions.DataIntegrityException;
import com.github.matheus321699.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	
	/*
	 * Método para buscar por Id
	 */
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));		
	}
	
	/*
	 * Método isert para inserir objeto no banco de dados 
	 */
	@Transactional
	public Cliente insert(Cliente obj) {
		// Setar Id para garantir que o objeto inserido é um novo registro
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}
	
	/*
	 * Método para atualizar dados no banco de dados
	 */
	public Cliente update(Cliente obj) {
		// Verificando se o objeto existe com o método find
		Cliente newObj = find(obj.getId());
		updateDate(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		/*
		 * Capturando exceção que é lançada quando um objeto que está associado
		 * a outro objeto sofre uma tentativa de deleção, não permitindo que o
		 * o objeto seja deletado.
		 */
		try {
		repo.deleteById(id);
		} 
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos"
					+ " relacionados!");
		}
		
	}
	/*
	 * DTO(Data Tranfer Object) ou Objeto de Transferência de Dados: É um objeto que possui
	 * somente os dados solicitados para alguma operação no sistema.
	 */
	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	/*
	 * Criando um serviço de paginação para buscar registros no banco de dados de acordo
	 * com uma quantidade determinada de regsitros.
	 */
	// Page encapsula informações e operações de paginações
	/*
	 * Parâmetro page: qual a página
	 * Parâmetro linesPage: Linhas por página
	 * Parâmetro orderBy: por qual atributo será ordenado
	 * Parâmetro : qual direção deve ser ordenado, ascendente ou descendente
	 */
	public Page<Cliente> findPage(Integer page, Integer linesPage, String orderBy, String direction){
		/* pageRequest: Objeto que vai preparar as informações para realizar uma consulta que 
		* retorne a página de dados
		*/
		PageRequest pageRequest = PageRequest.of(page, linesPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDTO) { 
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(), TipoCliente.toEnum(objDTO.getTipo()));
		Cidade cid = new Cidade(objDTO.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDTO.getTelefone1());
		
		if(objDTO.getTelefone2() != null) {
			cli.getTelefones().add(objDTO.getTelefone2());
		}
		if(objDTO.getTelefone3() != null) {
			cli.getTelefones().add(objDTO.getTelefone3());
		}
		return cli;
	}
	
	private void updateDate(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	
}