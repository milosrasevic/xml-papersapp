package xml.papersapp.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailClient {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Async
    public void sendEmail(String emailTo, String subject, String content) {

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

            messageHelper.setText(content, true);

            messageHelper.setFrom(emailFrom);
            messageHelper.setTo("ljubicjanko1@gmail.com");
            messageHelper.setSubject(subject);

        };
        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            e.printStackTrace();
            // runtime exception; compiler will not force you to handle it
        }





//
//
//
//
//
//
//        try {
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//            String text = String.format("You have been assigned as reviewer on paper : " + sciencePaperTitle);
//            try {
//                mimeMessage.setContent(text, "text/html");
//                helper.setTo("isamrs19mail@gmail.com");
//                helper.setSubject("Review assingment");
//                helper.setFrom(emailFrom);
//
//                javaMailSender.send(mimeMessage);
//                return true;
//            } catch (MessagingException e) {
//                e.printStackTrace();
//                return false;
//            }
//        } catch (MailException e) {
//            System.out.println(e);
//            System.err.println("Something went wrong.");
//            return false;
//        }
    }
}
