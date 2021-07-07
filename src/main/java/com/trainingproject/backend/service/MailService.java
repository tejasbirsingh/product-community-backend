package com.trainingproject.backend.service;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.trainingproject.backend.exceptions.ProductWebsiteException;
import com.trainingproject.backend.model.NotificationEmail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
class MailService {

	private final JavaMailSender mailSender;

	@Async
	void sendMail(NotificationEmail notificationEmail) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom("productcommunity@email.com");
			messageHelper.setTo(notificationEmail.getRecipient());
			messageHelper.setSubject(notificationEmail.getSubject());
			messageHelper.setText(notificationEmail.getBody(), true);

		};
		try {
			mailSender.send(messagePreparator);
			log.info("Email sent to user account");
		} catch (MailException e) {
			log.error("Exception occurred when sending mail", e);
			throw new ProductWebsiteException(
					"Exception occurred when sending mail to " + notificationEmail.getRecipient(), e);
		}
	}

}