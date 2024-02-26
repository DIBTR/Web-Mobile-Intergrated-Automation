package com.dibtr.test;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.dibtr.util.AllureLogger;
import com.dibtr.util.DeviceSupportUtility;
import com.dibtr.util.DriverFactory;
import com.dibtr.util.TestData;

public class SmokeTest extends DriverFactory {
  private static final AllureLogger LOGGER = new AllureLogger(SmokeTest.class);
  
  @Parameters({"platformVersion", "deviceName"})
  @Test(groups = {"Sanity"}, description = "")
  public void end2endTest() {

    // Generate Test input data
    TestData testData = new TestData();

    LOGGER.info("Using test data {}", testData);

    // Started - Driver App flow
    LOGGER.info("Starting Mobile application flow");
    this.startMobileAppSession("14.2", "iPhone 11", true);
    new DeviceSupportUtility().openApp();
    // Perform the actions
    this.stopMobileAppSession();

    // Started Web Flow
    LOGGER.info("Starting Web application flow");
    this.startBrowserSession();
    // Perform the web actions
    this.stopWebSession();
    // Finished Web flow
  }
}
