package com.dibtr.util;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.Listeners;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;


@Listeners({AllureTestListener.class})
public class DriverFactory {
  private static final AllureLogger LOGGER = new AllureLogger(DriverFactory.class);
  private Properties configProperties = EnvironmentSettings.getInstance().getProperties();
  private static final String ENVIRONMENT_LOCATION = "environment.location";
  private static ThreadLocal<IOSDriver<IOSElement>> iosDriver = new ThreadLocal<>();
  private static ThreadLocal<IOSPageDriver> pageDriver = new ThreadLocal<>();
  private ServerConfigurator serverConfigurator;
  private static ThreadLocal<WebDriver> webdriver = new ThreadLocal<>();
  private static ThreadLocal<WebPageDriver> webPageDriver = new ThreadLocal<>();
 
  /**
   * Tear down automation session. Here we are adding Suppress warning to avoid type cast issue.
   * 
   * @return The current thread's value of this thread-local
   */
  public static IOSDriver<IOSElement> getIOSDriver() {
    return iosDriver.get();
  }

  public static WebDriver getWebDriver() {
    return webdriver.get();
  }

  /**
   * Get the pageDriver.
   *
   * @return pageDriver - The page driver
   */
  public static IOSPageDriver getMobilePageDriver() {
    return pageDriver.get();
  }

  public static WebPageDriver getWebPageDriver() {
    return webPageDriver.get();
  }

  /**
   * Mobile start up. This method runs before each class during a test suite.</br>
   * </br>
   * Objectives:</br>
   * - Calls startDeviceSession to get desired capabilities and launch the App.</br>
   * - Initializes the context.</br>
   * - Builds a Map for the environment flags.
   */

  /**
   * Mobile start up. This method runs before each class during a test suite.
   * 
   * @param platformVersion - String - Model version value is like (13.0/ 13.2).This parameter ois
   *        optional when we are executing on Perfecto.
   * @param deviceName - String - Device name value is like (iPhone 8 /iPhone 11 etc).
   */
  public void startMobileAppSession(String platformVersion, String deviceName) {
    this.startMobileAppSession(platformVersion, deviceName, false);
  }

  public void startMobileAppSession(String platformVersion, String deviceName, boolean fullReset) {
    LOGGER.info("Trying to start device session with platform {} & device name {}", platformVersion,
        deviceName);

    switch (configProperties.getProperty(ENVIRONMENT_LOCATION).toLowerCase()) {
      case "localhost":
        LOGGER.info("Execution is going to start on local machine Simulator");
        serverConfigurator = new AppiumServerConfigurator();
        break;
      default:
        LOGGER.info("Wrong Execution platform is selected");
        break;
    }
    serverConfigurator.startDeviceSession(platformVersion, deviceName, fullReset);
    iosDriver.set(serverConfigurator.getWebDriver());
    pageDriver.set(serverConfigurator.getPageDriver());
    LOGGER.info("Device session started");
  }


  /**
   * Shuts down the web driver and closes the app. This method runs after each class during a test
   * suite.
   */
  public void stopMobileAppSession() {
    LOGGER.info("Device session stop process started");
    serverConfigurator.stopDeviceSession();
    iosDriver.remove();
    pageDriver.remove();
    LOGGER.info("Device session stopped");
  }


  public void startBrowserSession() {
    if (configProperties.getProperty("web.browser.name").equalsIgnoreCase("chrome")) {
      System.setProperty("webdriver.chrome.driver",
          configProperties.getProperty("web.chrome.driver.path"));
      webdriver.set(new ChromeDriver());
      webPageDriver.set(new WebPageDriver(getWebDriver()));
      LOGGER.info("Started Driver {} ", webdriver.get());
    } else if (configProperties.getProperty("web.browser.name").equalsIgnoreCase("firefox")) {

    }
  }

  public void stopWebSession() {
    SessionId sessionid = ((RemoteWebDriver) DriverFactory.getWebDriver()).getSessionId();
    if (sessionid != null) {
      DriverFactory.getWebDriver().quit();
    }
  }
}
