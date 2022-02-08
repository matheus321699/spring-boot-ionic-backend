package com.github.matheus321699.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.github.matheus321699.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	/*
	 * Anotação para fazer com que o framework pegue o valor que está no
	 * arquivo application.properties e armazene na strinf sender.
	 */
	@Value("${default.sender}")
	private String sender;
	
	@Autowired
	private TemplateEngine templateEngni;
	
	private JavaMailSender javaMailSender;
	
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
	
	protected String htmlFromTemplatePedido(Pedido obj) {
		
		// Objeto do thymeleaf para acessar template html
		Context context = new Context();
		
		// Enviando objeto de Pedido, com apelido de pedido, para o template html
		context.setVariable("pedido", obj);
		
		/* Processar template para retornar o html em forma de string, passando o
		* caminho do template e o objeto context
		*/
		return templateEngni.process("email/confirmacaoPedido", context);
	}
	
	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido obj) {
		try {
			MimeMessage mm = prepareMimeMailMessageFromPedido(obj);
			sendHtmlEmail(mm);
		} catch (MessagingException e) {
			
			// Método para enviar o texto plano
			sendOrderConfimationEmail(obj);
		}
	}
	
	protected MimeMessage prepareMimeMailMessageFromPedido(Pedido obj) throws MessagingException {
		
		// Usando objeto javaMailSender para instanciar um MimeMessage
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		// Objeto que permite atribuir valores a essa mensagem
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
	
		// Atrinuindo valores
		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Pedido confirmado! Código: " + obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		
		// Corpo do email vai ser o email html procesado a partir do template thymeleaf
		mmh.setText(htmlFromTemplatePedido(obj), true);
		
		
		return mimeMessage;
	}
}
