package com.dibtr.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.appium.java_client.MobileBy;

/**
 * The Class DeviceSupportPage.</br>
 * </br>
 * This class is for handling device specific functionality such as closing and reopening the
 * application or dealing with device specific popups.</br>
 * </br>
 * Other functionality may include device settings and restarting the device.
 */
public class DeviceSupportUtility {
  private static Properties configProperties = EnvironmentSettings.getInstance().getProperties();
  private static final Logger LOGGER = LoggerFactory.getLogger(DeviceSupportUtility.class);
  private static final String APP_BUNDLE_IDENTIFIER =
      configProperties.getProperty("mobile.appium.bundleId");
  private static final String BUNDLE_ID = "bundleId";

 
  /**
   * Checks if the Settings button is showing and cancels if it is not. Clicks on Settings button to
   * be taken to device settings. Changed the location setting from never to While using the app.
   * Returns to the app.
   */
  public void turnOnLocationServices() {
    String dom = DriverFactory.getMobilePageDriver().getHtmlSource();
    if (dom.contains("Settings")) {
      DriverFactory.getMobilePageDriver()
          .clickElement(MobileBy.iOSNsPredicateString("label CONTAINS 'Settings'"));
    } else {
      return;
    }
    DriverFactory.getMobilePageDriver()
        .clickElement(MobileBy.iOSNsPredicateString("label CONTAINS 'Location'"));
    DriverFactory.getMobilePageDriver()
        .clickElement(MobileBy.iOSNsPredicateString("label CONTAINS 'While Using the App'"));
    DriverFactory.getMobilePageDriver().clickElement(MobileBy.AccessibilityId("breadcrumb"));
  }

  /**
   * Closes and re-opens Choice application.
   */
  public void restartApplication() {
    this.closeApplication();
    this.startApp();
  }

  /**
   * Used to close the Choice - com.choicehotels.iosapp Only for Perfecto devices
   *
   * @param appName The application name
   */
  public void closeApplication() {
    LOGGER.info("Closing Application: {}", APP_BUNDLE_IDENTIFIER);
    DriverFactory.getIOSDriver().terminateApp(APP_BUNDLE_IDENTIFIER);
  }

  /**
   * This utility function is used to un-install existing installed app version, installed latest
   * version & launch app.
   */
  public void openApp() {
    //this.uninstallApp();
    //this.installApp();
    this.startApp();
  }

  /**
   * This function is used to Start App.
   */
  public void startApp() {
    Map<String, Object> params = new HashMap<>();
    params.put(BUNDLE_ID, APP_BUNDLE_IDENTIFIER);
    DriverFactory.getIOSDriver().executeScript("mobile: launchApp", params);
    LOGGER.info("App opened sucessfully.....");
  }

  /**
   * This function is used to install App.
   */
  public void installApp() {
    LOGGER.info("App installation started.....");
    Map<String, Object> params = new HashMap<>();
    params.put("app", DriverFactory.getIOSDriver().getCapabilities().getCapability("app"));
    DriverFactory.getIOSDriver().executeScript("mobile: installApp", params);
    LOGGER.info("App installation finished.....");
  }

  /**
   * This function is used to un-install app.
   */
  public void uninstallApp() {
    LOGGER.info("App is being removed from the device.....");
    Map<String, Object> params = new HashMap<>();
    try {
      params.put(BUNDLE_ID, APP_BUNDLE_IDENTIFIER);
      DriverFactory.getIOSDriver().executeScript("mobile:removeApp", params);
      LOGGER.info("On Simulator App uninstalled successfully");
    } catch (Exception ex) {
      LOGGER.error("Failed to uninstall app {} {} ", ex, ex.getMessage());
    }
  }

  /**
   * This function is used to reset app.
   */
  public void resetApp() {
    LOGGER.info("App reset process started  .....");
    Map<String, Object> params = new HashMap<>();
    try {
      params.put(BUNDLE_ID, APP_BUNDLE_IDENTIFIER);
      DriverFactory.getIOSDriver().resetApp();
      LOGGER.info("On Simulator App reset successfully");
    } catch (Exception ex) {
      LOGGER.error("Failed to reset app {}", ex.getMessage(), ex);
    }
  }

  /**
   * This function is used to Reboot device.
   */
  public void putDeviceInRebootMode() {
    Map<String, Object> pars = new HashMap<>();
    DriverFactory.getIOSDriver().executeScript("mobile:handset:reboot", pars);
  }
}
