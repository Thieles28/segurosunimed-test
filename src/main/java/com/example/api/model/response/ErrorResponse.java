package com.example.api.model.response;

import lombok.Data;

@Data
public class ErrorResponse {
  private Integer status;
  private String error;
  private String message;
  private Long timestamp;

  public ErrorResponse(int status, String error, String message) {
    this.status = status;
    this.error = error;
    this.message = message;
    this.timestamp = System.currentTimeMillis();
  }
}
