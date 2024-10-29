package com.vasylenkob.pastebin.services;

import com.vasylenkob.pastebin.exceptions.FailedToSendEmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String toAddress, String subject, String message){
        try {
            MimeMessage  mimeMessage = createMimeMessage(toAddress, subject, message);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | MailSendException e) {
            throw new FailedToSendEmailException("Failed to send email");
        }
    }
    private MimeMessage createMimeMessage(String toAddress, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage , true);

        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(message, true);
        return mimeMessage;
    }
}
