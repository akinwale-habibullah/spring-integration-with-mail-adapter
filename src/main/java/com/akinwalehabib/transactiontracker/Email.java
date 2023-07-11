package com.akinwalehabib.transactiontracker;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Document(collection = "emails")
public class Email {

  @Id
  private String id;
  private String email;
  private String subject;
  private Double amount;
  private String remarks;
  private LocalDateTime receiptDate;
  private String content;

  public Email (String email, String subject, String content) {
    this.email = email;
    this.subject = subject;
    this.content = content.toUpperCase();
    
    this.amount = retrieveAmount(content);
    this.receiptDate = retrieveReceiptDate(content);
    this.remarks = retrieveRemarks(content);
  }

  public double retrieveAmount(String content) {
    Pattern pattern = Pattern.compile("Amount : NGN [1-9]\\d?(?:,\\d{3})*(?:\\.\\d{2})");
    Matcher matcher = pattern.matcher(content);
    String amountString = "";

    if (matcher.find()) {
      String substring = matcher.group();
      String[] substringParts = substring
        .split(":");                                        // ["Amount", ":", "NGN 5,000.00"]
      String[] substringAmountPart = substringParts[1]
        .trim()
        .split(" ");                                        // ["NGN 5,000.00"]
      amountString = substringAmountPart[1].replace(",", "");
      double value = Double.parseDouble(amountString);
      return value;
    }
    
    return 0.0;
  }

  public String retrieveRemarks(String content) {
    Pattern pattern = Pattern.compile("Remarks :([\\s]+\\w+\\s+[\\w]+)+");
    Matcher matcher = pattern.matcher(content);
    String remarks = "";

    if (matcher.find()) {
      String substring = matcher.group();
      remarks = substring.split(":")[1]
        .trim();
    }

    return remarks;
  }

  public LocalDateTime retrieveReceiptDate(String content) {
    Pattern pattern = Pattern.compile("Time of Transaction : (\\d+-\\d+-\\d+ \\d+\\d+:\\d+)");
    Matcher matcher = pattern.matcher(content);
    LocalDateTime receiptDate = LocalDateTime.now();

    if (matcher.find()) {
      String substring = matcher.group();                                 // Time of Transaction : 21-06-2023
      String[] DateTimeParts = substring.split(":");                // ["Time of Transaction", "21-06-2023 16", "21"]
      String[] DateParts = DateTimeParts[1].trim().split("-");      // ["21", "06", "2023 16"]

      int day = Integer.parseInt(DateParts[0]);
      int month = Integer.parseInt(DateParts[1]);
      int year = Integer.parseInt(DateParts[2].substring(0, DateParts[2].length() - 3));
      
      int hour = Integer.parseInt(DateParts[2]
        .substring(DateParts[2].length() - 2)
        .trim());
      int minute = Integer.parseInt(DateTimeParts[2]);
      receiptDate = LocalDateTime.of(year, month, day, hour, minute);
    }

    return receiptDate;
  }
}
