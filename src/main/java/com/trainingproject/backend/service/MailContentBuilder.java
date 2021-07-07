package com.trainingproject.backend.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailContentBuilder {

	private final TemplateEngine templateEngine;

	public String build(String message, String url) {
		Context context = new Context();
		context.setVariable("message", message);
		context.setVariable("url", url);

		return templateEngine.process("mailTemplate", context);
	}
}	