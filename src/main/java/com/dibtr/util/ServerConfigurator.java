package com.dibtr.util;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;


public interface ServerConfigurator {


  public void startDeviceSession(String platformVersion, String deviceName);
  
  public void startDeviceSession(String platformVersion, String deviceName, boolean fullReset);

 
  public IOSDriver<IOSElement> getWebDriver();


  public IOSPageDriver getPageDriver();

 
  public void stopDeviceSession();

 

}
