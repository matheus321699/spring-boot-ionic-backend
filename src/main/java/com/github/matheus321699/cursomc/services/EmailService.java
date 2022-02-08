package com.github.matheus321699.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.github.matheus321699.cursomc.domain.Pedido;

public interface EmailService {

	// Método de envio de email de confirmação de pedidos
	void sendOrderConfimationEmail(Pedido obj);
	
	// Enviar email com texto plano
	void sendEmail(SimpleMailMessage msg);
	
	void sendOrderConfirmationHtmlEmail(Pedido obj);
	
	// Enviar email em html
	void sendHtmlEmail(MimeMessage msg);
	
}
