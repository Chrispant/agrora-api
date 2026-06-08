package gr.agrora_api;

import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.resend.*;

@Service
public class EmailService {


    private final String recipientEmail;
    private final Resend resend;

    public EmailService(
            @Value("${contact.recipient-email}") String recipientEmail,
            @Value("${resend.api.key}") String apiKey
    )
    {

        this.recipientEmail = recipientEmail;
        this.resend = new Resend(apiKey);
    }

    public void sendContactMessageNotification(ContactMessage contactMessage) throws ResendException {
        String htmlContent = String.format(
                        "<h2>New Contact Message</h2>" +
                        "<p><strong>From:</strong> %s</p>" +
                        "<p><strong>Email:</strong> %s</p>" +
                        "<p><strong>Message:</strong></p><p>%s</p>",
                contactMessage.getName(),
                contactMessage.getEmail(),
                contactMessage.getMessage()
        );

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("Agrora Team <info@agrora.gr>")
                .to(recipientEmail)
                .replyTo(contactMessage.getEmail())
                .subject("New Contact Message from Agrora.gr!")
                .html(htmlContent)
                .build();

        SendEmailResponse data = resend.emails().send(sendEmailRequest);

        System.out.println("Email sent successfully! ID: " + data.getId());


    }
}