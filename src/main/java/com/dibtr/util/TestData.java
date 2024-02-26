package com.dibtr.util;

public class TestData {

  private String note = CommonUtility.getRandomAlphabeticString(10);
  private String testDescription = CommonUtility.getRandomAlphabeticString(10);
  private String usernameValue = CommonUtility.getRandomAlphabeticString(6) + "@gmail.com";
  private String passwordValue = CommonUtility.getRandomAlphabeticString(4) + "@123";
  private String firstName = CommonUtility.getRandomAlphabeticString(6);
  private String lastName = CommonUtility.getRandomAlphabeticString(6);
  private String phoneNumber = "0123456789";
  private String mile = "10";

  public TestData() {}

  public void setNote(String note) {
    this.note = note;
  }

  public String getNote() {
    return note;
  }

  public String getTestDescription() {
    return testDescription;
  }

  public String getRandomUsername() {
    return usernameValue;
  }

  public String getRandomPassword() {
    return passwordValue;
  }

  public String getRandomFirstName() {
    return firstName;
  }

  public String getRandomLastName() {
    return lastName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getMiles() {
    return mile;
  }
}

