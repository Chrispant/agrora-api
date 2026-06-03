package gr.agrora_api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

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
        mailSender.send(message);


    }
}