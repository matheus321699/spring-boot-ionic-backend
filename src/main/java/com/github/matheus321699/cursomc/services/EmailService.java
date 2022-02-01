package com.github.matheus321699.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.github.matheus321699.cursomc.domain.Pedido;

public interface EmailService {

	// Método de envio de email de confirmação de pedidos
	void sendOrderConfimationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
}
