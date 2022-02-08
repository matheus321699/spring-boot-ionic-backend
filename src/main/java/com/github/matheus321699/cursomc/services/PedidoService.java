package com.github.matheus321699.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.matheus321699.cursomc.domain.Categoria;
import com.github.matheus321699.cursomc.domain.ItemPedido;
import com.github.matheus321699.cursomc.domain.PagamentoComBoleto;
import com.github.matheus321699.cursomc.domain.Pedido;
import com.github.matheus321699.cursomc.domain.enums.EstadoPagamento;
import com.github.matheus321699.cursomc.repositories.ItemPedidoRepository;
import com.github.matheus321699.cursomc.repositories.PagamentoRepository;
import com.github.matheus321699.cursomc.repositories.PedidoRepository;
import com.github.matheus321699.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	/*
	 * Instancia do EmailService para pegar resposta do corpo do
	 * email enviado.
	 */
	@Autowired
	private EmailService emailService;
	
	/*
	 * Método para buscar por Id
	 */
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));	
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		// Usar Id para pegar cliente no banco de dados.
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		/*
		 * Se o pagamento for igual a pagamento com boleto
		 */
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		/*
		 * Salvando pedido, pagamento e itens pedido no banco de dados
		 */
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip : obj.getItens()) {
			// Mostrar produto sem desconto
			ip.setDesconto(0.0);
			// Setar produto
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
}