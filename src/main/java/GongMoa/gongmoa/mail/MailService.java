package GongMoa.gongmoa.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;

    private String getAuthCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        while(sb.length() < 6) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public void sendMail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailMessage.getMessage(), true);
            mailSender.send(mimeMessage);
            log.info("sent email to: {}", emailMessage.getTo());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
