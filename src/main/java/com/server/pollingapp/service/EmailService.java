package com.server.pollingapp.service;

import com.server.pollingapp.models.UserModel;
import io.rocketbase.commons.colors.ColorPalette;

import io.rocketbase.commons.email.EmailTemplateBuilder;
import io.rocketbase.commons.email.model.HtmlTextEmail;
import io.rocketbase.commons.email.template.styling.FontWeight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    final JavaMailSender javaMailSender;


    @Value("${app.frontend}")
    private String website;

    @Value("${app.email}")
    private String emailToBeUsed;

    @Autowired
    public EmailService(@Lazy JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void createActivationTemplate(String token, UserModel userModel){
        EmailTemplateBuilder.EmailTemplateConfigBuilder builder = EmailTemplateBuilder.builder();
        String header = "ACCOUNT ACTIVATION";
        HtmlTextEmail htmlTextEmail = builder
                .header(header)
                .and()
                .logo("https://www.rocketbase.io/img/logo-dark.png","",0,41)
                .and()
                .addText("Welcome," +userModel.getUsername() +"!").fontWeight(FontWeight.BOLD).center()
                .and()
                .addText("Thanks for Registering with Votex. We’re thrilled to have you on board. To get started you will need to verify your Account:")
                .and()
                .addButton("Verify Account", "https://localhost:8443/api/v1/auth/activate/"+token).color(ColorPalette.FRENCH_BLUE)
                .and()
                .addHtml(
                        "If you have any questions, feel free to <a href=poll-helpdesk-3482a7@inbox.mailtrap.io>email</a> our customer service team. (We're lightning quick at replying.) We also offer live chat during business hours."
                ).and()
                .addText(
                        " Cheers, The Votex Team "
                ).and()
                .copyright("votex").url(website)
                .and()
                .addPlainTextFooter(
                        "[Votex, LLC]," +
                        "1234 Street Rd,"+
                         "Suite 1234"
                ).and()
                .addImage("https://cdn.rocketbase.io/assets/loading/no-image.jpg","",100,0)
                .linkUrl(website).and()
                .build();
           SendAccountActivationEmail( htmlTextEmail, userModel);
    }
    public void SendAccountActivationEmail(HtmlTextEmail htmlTextEmail,UserModel userModel){
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = null;
        try {
            messageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        } catch (MessagingException e) {

        }
        try {
            assert messageHelper != null;
            messageHelper.setTo( userModel.getEmail());
            messageHelper.setSubject("Votex Account Activation");
            messageHelper.setText(htmlTextEmail.getText(),htmlTextEmail.getHtml());
            messageHelper.setFrom(emailToBeUsed);
        } catch (MessagingException e) {

        }
        try {
            javaMailSender.send(message);
        }
        catch (MailException e) {
    }

    }
}
