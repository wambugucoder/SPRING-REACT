package com.server.pollingapp.service;

import com.server.pollingapp.models.UserModel;
import io.rocketbase.commons.colors.ColorPalette;
import io.rocketbase.commons.email.EmailTemplateBuilder;
import io.rocketbase.commons.email.model.HtmlTextEmail;
import io.rocketbase.commons.email.template.styling.FontWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {


    @Value("${app.frontend}")
    private String website;

    @Value("${app.email}")
    private String emailToBeUsed;

     @Value("${app.url}")
    private String appUrl;

    

    Logger log= LoggerFactory.getLogger(EmailService.class);

    @Autowired
    JavaMailSender javaMailSender;

    public void createActivationTemplate(String token, UserModel userModel){
        EmailTemplateBuilder.EmailTemplateConfigBuilder builder = EmailTemplateBuilder.builder();
        String header = "ACCOUNT ACTIVATION";
        HtmlTextEmail htmlTextEmail = builder
                .header(header)
                .and()
                .logo("https://netstorage-tuko.akamaized.net/images/5cd3d44ba68de436.png","",0,41)
                .and()
                .addText("Welcome," +userModel.getUsername() +"!").fontWeight(FontWeight.BOLD).center()
                .and()
                .addText("Thanks for Registering to the JKUAT Election System. We’re thrilled to have you on board. To get started you will need to verify your Student Account:")
                .and()
                .addButton("Verify Account", appUrl+token).color(ColorPalette.FRENCH_BLUE)
                .and()
                .addHtml(
                        "If you have any questions, feel free to <a href=poll-helpdesk-3482a7@inbox.mailtrap.io>email</a> our customer service team. (We're lightning quick at replying.) We also offer live chat during business hours."
                ).and()
                .addText(
                        " Cheers, The JKUAT Electoral Commission Team "
                ).and()
                .copyright("JKUATx").url(website)
                .and()
                .addPlainTextFooter(
                        "[JKUAT, EC]," +
                        "1234 JUJA,"+
                         "Suite 1234"
                ).and()
                .addImage("https://netstorage-tuko.akamaized.net/images/5cd3d44ba68de436.png","",100,0)
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
            log.error(e.getLocalizedMessage());
        }
        try {
            assert messageHelper != null;
            messageHelper.setTo( userModel.getEmail());
            messageHelper.setSubject("JKUAT Election Account Activation");
            messageHelper.setText(htmlTextEmail.getText(),htmlTextEmail.getHtml());
            messageHelper.setFrom(emailToBeUsed);

        } catch (MessagingException e) {
            log.error(e.getLocalizedMessage());
        }
        try {
            javaMailSender.send(message);
        }
        catch (MailException e) {
            log.error(e.getLocalizedMessage());
    }

    }
}
