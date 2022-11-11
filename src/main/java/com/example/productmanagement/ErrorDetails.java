package com.example.productmanagement;

import java.util.Date;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorDetails {
  private Date timestamp;
  private String message;
  private String details;

  public Date getTimeSramp() {
    return timestamp;
  }

  public String getMessage() {
    return message;
  }

  public String getDetails() {
    return details;
  }
}
