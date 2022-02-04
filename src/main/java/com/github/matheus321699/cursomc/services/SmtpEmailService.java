package com.github.matheus321699.cursomc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpEmailService extends AbstractEmailService {

	@Autowired
	/*
	 * Calsse que permite instanciar um objeto com todos os dados passados para
	 * o arquivo de propriedade.
	 */
	private MailSender mailSender;
	
	// Objeto logger do java referente a essa classe
	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Enviando eamil...");
		mailSender.send(msg);
		LOG.info("Email enviado");	
	}
	
	

}
