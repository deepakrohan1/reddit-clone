package com.example.demo.service;

import com.example.demo.exception.MailSenderApplicationException;
import com.example.demo.model.NotificationEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailContentBuilder mailContentBuilder;

    public void sendEmail(NotificationEmail notificationEmail) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("someemail@email.com");
            mimeMessageHelper.setTo(notificationEmail.getRecipient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
            mimeMessage.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };

        try {
            javaMailSender.send(mimeMessagePreparator);
            log.info("Activation mail sent");
        } catch (MailException ex) {
            log.error(ex.toString());
            throw  new MailSenderApplicationException("Unable to send mail to: "+ notificationEmail.getRecipient());
        }
    }
}
