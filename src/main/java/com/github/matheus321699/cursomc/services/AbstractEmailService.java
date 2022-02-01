package com.github.matheus321699.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.github.matheus321699.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	/*
	 * Anotação para fazer com que o framework pegue o valor que está no
	 * arquivo application.properties e armazene na strinf sender.
	 */
	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendOrderConfimationEmail(Pedido obj) {
		// Intanciar um SimpleMailMessage a partir do meu obj
		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(obj);
		sendEmail(sm);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());
		
		// Setando remetente
		sm.setFrom(sender);
		
		// Setando assunto do email
		sm.setSubject("Pedido confirmado! Código: " + obj.getId());
		
		// Setar data do envio do email com o horário do meu servidor
		sm.setSentDate(new Date(System.currentTimeMillis()));
		
		// Setando texto do corpo do email enviado
		sm.setText(obj.toString());
		
		return sm;
	}
	
}
