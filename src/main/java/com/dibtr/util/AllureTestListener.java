package com.dibtr.util;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import io.qameta.allure.Attachment;

public class AllureTestListener implements ITestListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(AllureTestListener.class);

  private static String getTestMethodName(ITestResult iTestResult) {
    return iTestResult.getMethod().getConstructorOrMethod().getName();
  }

  /**
   * This function is used embed failure screenshot within allure report.
   * 
   * @param driver - WebDriver - Driver instance.
   * @return - Screenshot file - Captured screenshot file in PNG format.
   */
  @Attachment(value = "Page screenshot", type = "image/png")
  public byte[] saveScreenshotPNG(WebDriver driver) {
    return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
  }

  /**
   * This function is used to embed test description within Allure report.
   * 
   * @param message - String - Test description message.
   * @return String - Failed test message string with test case name.
   */
  @Attachment(value = "{0}", type = "text/plain")
  public static String saveTextLog(String message) {
    return message;
  }

  @Override
  public void onStart(ITestContext iTestContext) {
    WebDriver driver = null;

    try {
      if (DriverFactory.getIOSDriver() != null) {
        driver = DriverFactory.getIOSDriver();
      } else {
        driver = DriverFactory.getWebDriver();
      }
    } catch (Exception e) {
      LOGGER.error("Got exception in onStart ", e);
    }

    iTestContext.setAttribute("WebDriver", driver);
  }

  @Override
  public void onFinish(ITestContext iTestContext) {
    try {
      File envProps =
          new File(System.getProperty("user.dir") + "/allure-results/environment.properties");
      FileUtils.copyFile(new File("config.properties"), envProps);
      LOGGER.info("Allure env properties path {}", envProps);
      LOGGER.info("{} was completed and config.properties moved to allure report directory.",
          iTestContext.getName());
    } catch (IOException e) {
      LOGGER.error("Unable to copy config.properties to allure report directory", e);
    }
  }

  @Override
  public void onTestStart(ITestResult iTestResult) {
    LOGGER.info("On Test Start {}-{}", iTestResult.getMethod().getMethodName(),
        iTestResult.getMethod().getDescription());
  }

  @Override
  public void onTestSuccess(ITestResult iTestResult) {
    System.out.println("*****");
    LOGGER.info("$$$ Pass: {}-{}", iTestResult.getMethod().getMethodName(),
        iTestResult.getMethod().getDescription());
  }

  @Override
  public void onTestFailure(ITestResult iTestResult) {
    WebDriver driver = null;
    try {
      if (DriverFactory.getIOSDriver() != null) {
        driver = DriverFactory.getIOSDriver();
      } else {
        driver = DriverFactory.getWebDriver();
      }
    } catch (Exception e) {
      LOGGER.error("Got exception in onTestFailure ", e);
    }
    LOGGER.info("Screenshot captured for test case: {} ", getTestMethodName(iTestResult));
    saveScreenshotPNG(driver);
    saveTextLog(getTestMethodName(iTestResult) + " failed and screenshot taken!");
  }

  @Override
  public void onTestSkipped(ITestResult iTestResult) {
    LOGGER.info("@@@ Skipped: {} {} {}", iTestResult.getMethod().getMethodName(), "-",
        iTestResult.getMethod().getDescription());
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {}

}
