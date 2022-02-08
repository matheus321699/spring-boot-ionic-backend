package com.github.matheus321699.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService{

	// Objeto logger do java referente a essa classe
	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Simulando evio de eamil...");
		LOG.info(msg.toString());
		LOG.info("Email enviado");	
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("Simulando evio de eamil HTML...");
		LOG.info(msg.toString());
		LOG.info("Email enviado");	
		
	}

	
}
