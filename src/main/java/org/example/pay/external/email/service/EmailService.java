package org.example.pay.external.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.example.pay.global.RandomGenerator;
import org.example.pay.global.exception.PayException;
import org.example.pay.global.exception.code.MailErrorCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final RedisTemplate<String, String> redisTemplate;

    private static final long VERIFICATION_CODE_TTL = 10;

    public String sendEmailVerificationCode(final String email) {
        if (!StringUtils.hasText(email)) {
            throw new PayException(MailErrorCode.MISSING_EMAIL);
        }

        String verificationCode = RandomGenerator.generateSecureRandomString();

        try {
            sendToEmail(email, verificationCode);
        } catch (MessagingException e) {
            throw new PayException(MailErrorCode.EMAIL_SENDING_ERROR);
        }

        redisTemplate.opsForValue().set(email, verificationCode);
        redisTemplate.expire(email, VERIFICATION_CODE_TTL, TimeUnit.MINUTES);

        return verificationCode;
    }

    public boolean verifyEmailCode(final String email, final String code) {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(code)) {
            return false;
        }

        String storedCode = redisTemplate.opsForValue().get(email);

        if (storedCode == null || !storedCode.equals(code)) {
            return false;
        }

        redisTemplate.delete(email);
        return true;
    }

    private void sendToEmail(String to, String verificationCode) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("인증 코드 안내");

        Context context = new Context();
        context.setVariable("code", verificationCode);

        String htmlContent = templateEngine.process("mail", context);
        helper.setText(htmlContent, true);

        emailSender.send(message);
    }
}