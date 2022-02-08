package com.github.matheus321699.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpEmailService extends AbstractEmailService {

	@Autowired
	/*
	 * Calsse que permite instanciar um objeto com todos os dados passados para
	 * o arquivo de propriedade.
	 */
	private MailSender mailSender;
	
	/*
	 * Classe que envia um MimeMessage
	 */
	@Autowired
	private JavaMailSender javaMailSender;
	
	// Objeto logger do java referente a essa classe
	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Enviando eamil...");
		mailSender.send(msg);
		LOG.info("Email enviado");	
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("Enviando eamil...");
		javaMailSender.send(msg);
		LOG.info("Email enviado");	
		
	}
	
	

}
