package xml.papersapp.service.mail;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import xml.papersapp.service.mail.EmailClient;

import java.io.IOException;

@Component
public class AssigmentEmailService {

    private static final String EMAIL_SUBJECT = "Review assigment";

    private EmailClient emailClient;

    public AssigmentEmailService(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    public void sendEmailForPurchasedTickets(String emailTo, String title) throws IOException {

        String content = generateContent(title);

        emailClient.sendEmail(
                emailTo,
                EMAIL_SUBJECT,
                content
        );
    }

    private String generateContent(String title) throws IOException {

        String msg = "";
        msg += "<html><body>";

        msg += "<p> You have been assingned to review paper with id " + title + ".";

        msg += "</body></html>";

        return msg;
    }

}
