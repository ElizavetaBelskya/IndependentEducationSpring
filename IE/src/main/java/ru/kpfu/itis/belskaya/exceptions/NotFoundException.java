package ru.kpfu.itis.belskaya.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{

  protected String entity = "page";

  public NotFoundException() {
    super();
  }

  public NotFoundException(String entity) {
    super();
    this.entity = entity;
  }

  public String getEntity(){
    return entity;
  }
}
