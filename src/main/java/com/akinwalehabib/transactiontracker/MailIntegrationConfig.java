package com.akinwalehabib.transactiontracker;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.dsl.Mail;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;

@Configuration
public class MailIntegrationConfig {

  private final String SUBJECT_KEYWORDS = "TRANSACTION ALERT";
  
  @Bean
  public IntegrationFlow mainIntegration(
    EmailProperties props,
    EmailTransformer emailTransformer,
    EmailRepository emailRepository
  ) {
    return IntegrationFlow
      .from(
        Mail.imapInboundAdapter(props.getImapUrl())
          .shouldDeleteMessages(false)
          .simpleContent(true)
          .autoCloseFolder(false),
        e -> e.poller(
          Pollers.fixedDelay(props.getPollRate())
        )
      )
      .<Message>filter((Message) -> {
        boolean containsKeyword = false;
        try {
          containsKeyword = Message.getSubject().toUpperCase().contains(SUBJECT_KEYWORDS);
        } catch (MessagingException e1) {
          e1.printStackTrace();
        }

        return containsKeyword;
      })
      .transform(emailTransformer)
      .handle(message -> {
        Email email = (Email) message.getPayload();
        System.out.println("New Email received: " + email);
        emailRepository.save(email);
      })
      .get();
  }
}
