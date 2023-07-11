package com.akinwalehabib.transactiontracker;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/payments",
                produces = "application/json")
public class EmailIntegrationAPI {
  
  private EmailRepository emailRepository;

  public EmailIntegrationAPI(EmailRepository emailRepository) {
    this.emailRepository = emailRepository;
  }

  @GetMapping
  public List<Email> getPaymenets() {
    List<Email> payments = emailRepository.findAll();
    return payments;
  }
}
