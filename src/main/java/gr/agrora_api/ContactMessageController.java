package gr.agrora_api;

import com.resend.core.exception.ResendException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/contact")
public class ContactMessageController {

    private final ContactMessageRepository contactMessageRepository;
    private final EmailService emailService;

    public ContactMessageController(ContactMessageRepository contactMessageRepository, EmailService emailService) {
        this.contactMessageRepository = contactMessageRepository;
        this.emailService = emailService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactMessage createContactMessage(@Valid @RequestBody ContactMessageRequest request) throws ResendException {
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setName(request.getName());
        contactMessage.setEmail(request.getEmail());
        contactMessage.setMessage(request.getMessage());

        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);
        emailService.sendContactMessageNotification(savedMessage);

        return savedMessage;
    }

}
