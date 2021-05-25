package com.server.pollingapp.response;

import java.io.Serializable;

/**
 * @author Jos Wambugu
 * @since 13-04-2021
 */
public class UniversalResponse implements Serializable {
  private static final long serialVersionUID = 2269386659574330817L;
  public String message;
  public Boolean error;

  public UniversalResponse() {}

  public void setMessage(String message) { this.message = message; }

  public void setError(Boolean error) { this.error = error; }
}
