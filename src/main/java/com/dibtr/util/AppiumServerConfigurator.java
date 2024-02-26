package com.dibtr.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

public class AppiumServerConfigurator implements ServerConfigurator {
  private static final AllureLogger LOGGER = new AllureLogger(AppiumServerConfigurator.class);
  private Properties configProperties = EnvironmentSettings.getInstance().getProperties();
  private static ThreadLocal<IOSDriver<IOSElement>> iosDriver = new ThreadLocal<>();
  private static ThreadLocal<IOSPageDriver> pageDriver = new ThreadLocal<>();

  /**
   * This function is used to Spin up device as per user device requirement & to start test session
   * on it.
   * 
   * @param platformVersion - String - Model version value, its nothing but OS version like
   *        (13.1/12.0 etc).
   * @param deviceName - String - Device name value, its nothing but device name like
   *        (iPhone-8/iPhone-11 etc).
   */
  @Override
  public void startDeviceSession(String platformVersion, String deviceName) {
    this.startDeviceSession(platformVersion, deviceName, false);
  }

  @Override
  public void stopDeviceSession() {
    LOGGER.info("Stopping device session");
    try {
      iosDriver.get().closeApp();
      iosDriver.get().quit();
      LOGGER.info("Stopped device session");
    } catch (Exception e) {
      LOGGER.error("Failed to close device session{} ", e);
    }
  }

  @Override
  public IOSDriver<IOSElement> getWebDriver() {
    return iosDriver.get();
  }

  @Override
  public IOSPageDriver getPageDriver() {
    return pageDriver.get();
  }

  @Override
  public void startDeviceSession(String platformVersion, String deviceName, boolean fullReset) {
    // Set Capabilities
    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
    desiredCapabilities.setCapability("automationName", "appium");
    desiredCapabilities.setCapability("platformName", "ios");
    desiredCapabilities.setCapability("platformVersion", platformVersion);
    desiredCapabilities.setCapability("deviceName", deviceName);
    desiredCapabilities.setCapability("app",
        configProperties.getProperty("mobile.device.app.path.local"));
    desiredCapabilities.setCapability("bundleId",
        configProperties.getProperty("mobile.appium.bundleId"));
    desiredCapabilities.setCapability("xcodeOrgId",
        configProperties.getProperty("mobile.appium.xcodeOrgId"));
    desiredCapabilities.setCapability("xcodeSigningId",
        configProperties.getProperty("mobile.appium.xcodeSigningId"));
    // desiredCapabilities.setCapability("udid",
    // configProperties.getProperty("mobile.device.udid"));
    desiredCapabilities.setCapability("showIosLog", true);
    desiredCapabilities.setCapability("showXcodeLog", true);
    if (fullReset) {
      desiredCapabilities.setCapability("fullReset", true);
    }
    URL gridURL = null;
    try {
      gridURL = new URL(configProperties.getProperty("environment.local.grid.location"));
    } catch (MalformedURLException e) {
      LOGGER.error("Error: URL Malformed provided for grid URL.", e);
    }
    try {
      LOGGER.info("{} {}", gridURL, desiredCapabilities);
      iosDriver.set(new IOSDriver<>(gridURL, desiredCapabilities));
      pageDriver.set(new IOSPageDriver(getWebDriver()));
      iosDriver.get().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    } catch (Exception e) {
      LOGGER.error("Used Desired Capabilities {} :", desiredCapabilities);
      LOGGER.error("Failed to start Appium session.", e);
    }

  }
}
