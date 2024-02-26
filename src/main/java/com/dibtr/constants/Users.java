package com.dibtr.constants;

import java.security.SecureRandom;

public class Users {
  
  public enum User {

    USER_1("",""),
    MANAGE_USER_1("",""),
    MANAGE_USER_2("",""),
    UNMANAGE_USER_1("",""),
    UNMANAGE_USER_2("","");

    private String username;
    private String password;

    User(String username, String passowrd ) {
      this.username = username;
      this.password=passowrd;
    }

    public String getUsername() {
      return username;
    }
    
    public String getPassword() {
      return password;
    }

    public static User getRandom() {
      SecureRandom random = new SecureRandom();
      return values()[random.nextInt(values().length)];
    }
  }

}
