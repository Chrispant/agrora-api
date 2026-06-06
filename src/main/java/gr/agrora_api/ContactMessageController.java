package gr.agrora_api;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;


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
    @CrossOrigin(origins = "https://agrora.gr",methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    @ResponseStatus(HttpStatus.CREATED)
    public ContactMessage createContactMessage(@Valid @RequestBody ContactMessageRequest request) {
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setName(request.getName());
        contactMessage.setEmail(request.getEmail());
        contactMessage.setMessage(request.getMessage());

        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);
        emailService.sendContactMessageNotification(savedMessage);

        return savedMessage;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    @CrossOrigin(origins = "https://agrora.gr", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public void handleOptions() {
        // Preflight handler
    }

}
