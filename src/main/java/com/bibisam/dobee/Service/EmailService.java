package com.bibisam.dobee.Service;

import com.bibisam.dobee.DTO.Auth.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

   // private final RedisUtils redisUtils;

    @Async
   public void sendEmail(EmailRequest request){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(request.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(request.getSubject()); // 메일 제목
            mimeMessageHelper.setText(request.getText(), false); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            log.info("code : {}, message : {}", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());

            LocalDate now = LocalDate.now();
        } catch (MessagingException e) {
            log.error(e.getMessage());
            log.info("code : {}, message : {}", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            throw new RuntimeException(e);
        }
   }

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        javaMailSender.send(message);
    }

    // 인증번호 및 임시 비밀번호 생성 메서드
    public String createCode(int n) {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < n; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0:
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
                default:
                    key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }
}
