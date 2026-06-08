package gr.agrora_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String recipientEmail;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${contact.recipient-email}") String recipientEmail
    )
    {
        this.mailSender = mailSender;
        this.recipientEmail = recipientEmail;
    }

    public void sendContactMessageNotification(ContactMessage contactMessage){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipientEmail);
        message.setReplyTo(contactMessage.getEmail());
        message.setSubject(" New contact message from Agrora.gr!");
        message.setText("""
                New Message!
                Name : %s
                Email : %s
               
                Message : %s""".formatted(contactMessage.getName(),contactMessage.getEmail(),contactMessage.getMessage()
                ));

        logger.info("Attempting to send contact notification email to {} for message id {}",
                recipientEmail, contactMessage.getId());
        try {
            mailSender.send(message);
            logger.info("Contact notification email sent successfully to {} for message id {}",
                    recipientEmail, contactMessage.getId());
        } catch (MailException e) {
            logger.error("Failed to send contact notification email to {} for message id {}: {}",
                    recipientEmail, contactMessage.getId(), e.getMessage(), e);
        }
    }
}