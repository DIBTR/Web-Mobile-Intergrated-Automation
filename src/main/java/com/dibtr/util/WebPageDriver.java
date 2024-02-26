package com.dibtr.util;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.appium.java_client.ios.IOSElement;

public class WebPageDriver {
  private static final AllureLogger LOGGER = new AllureLogger(WebPageDriver.class);
  //private static final Logger LOGGER = LoggerFactory.getLogger(WebPageDriver.class);
  private WebDriver webDriver;
  private int defaultWaitTimeout = 60;
  private static final String LOG_FORMAT = "{} : {}";
  private static final String ELEMENT_NOT_CLICKABLE = "Element not clickable";
  private static final String ELEMENT_NOT_FOUND = "Element not found.";
  private static final String ELEMENT_FOUND = "Element found.";
  private static final String FOUND = " Found: ";

  public WebPageDriver(WebDriver webDriver) {
    this.webDriver = webDriver;
  }

  public WebDriver getWebDriver() {
    return webDriver;
  }

  public void typeValueInField(final By by, final String value) {
    LOGGER.info("Trying to enter value {}  in field {}  ", by, value);
    waitForElement(by);
    WebElement webElement = webDriver.findElement(by);
    if (webElement != null) {
      webElement.click();
      webElement.clear();
      webElement.sendKeys(value);
    }
  }

  public void waitForElement(final By by) {
    waitForElement(by, defaultWaitTimeout);
  }

  private int calculatePollingTime(int timeout) {
    return (timeout <= 60) ? 500 : 1000;
  }

  public void waitForElement(final By by, final int timeout) {
    try {
      Wait<WebDriver> wait =
          new FluentWait<WebDriver>(webDriver).withTimeout(Duration.ofSeconds(timeout))
              .pollingEvery(Duration.ofMillis(calculatePollingTime(timeout)))
              .ignoring(NoSuchElementException.class);
      wait.until(new Function<WebDriver, WebElement>() {
        public WebElement apply(WebDriver driver) {
          return driver.findElement(by);
        }
      });
    } catch (NoSuchElementException e) {
      LOGGER.info("An Error has occured in waitForElement(final By by): {}", e.getMessage());
    }
  }

  public void clickElement(final By by) {
    clickElement(by, defaultWaitTimeout);
  }

  public void clickElement(final By by, final int timeout) {
    LOGGER.info("Trying to click on {}", by);
    waitForElementToBeClickable(by, timeout);
    webDriver.findElement(by).click();
  }

  public void setBrowserURL(String url) {
    DriverFactory.getWebDriver().manage().window().maximize();
    DriverFactory.getWebDriver().get(url);
  }

  public void waitForElementToBeClickable(final By by, final int timeout) {
    WebElement element =
        new WebDriverWait(DriverFactory.getWebDriver(), timeout, calculatePollingTime(timeout))
            .until(ExpectedConditions.elementToBeClickable(by));
    if (element == null) {
      LOGGER.info(LOG_FORMAT, ELEMENT_NOT_CLICKABLE, by);
    } else {
    }
  }

  public void pause(final int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      LOGGER.error("ERROR: An InterruptedException has occurred in PageDriver.pause()", e);
      Thread.currentThread().interrupt();
    }
  }

  public List<WebElement> getWebElementList(final By by) {
    return this.getWebElementList(by, defaultWaitTimeout);
  }

  /**
   * To get a list of WebElements represented by a common tag.
   * 
   * @param by - By - The location of the element
   * @param timeout - int - The period of time to wait in seconds
   * @return List<IOSElement> - List of iOS web elements represented by a common tag
   */
  public List<WebElement> getWebElementList(final By by, int timeout) {
    LOGGER.info("Attempting to retrieve a list of web elements with the {} tag.", by);
    Wait<WebDriver> wait =
        new FluentWait<WebDriver>(getWebDriver()).withTimeout(Duration.ofSeconds(timeout))
            .pollingEvery(Duration.ofMillis(500)).ignoring(NoSuchElementException.class);
    return wait.until((WebDriver driver) -> driver.findElements(by));
  }

  public WebElement getElement(By by, int waitTimeOut) {
    WebElement element = null;
    LOGGER.info("Trying to find element with visibility check");
    Wait<WebDriver> waitWithVisiblityCheck =
        new FluentWait<WebDriver>(getWebDriver()).withTimeout(Duration.ofSeconds(waitTimeOut))
            .pollingEvery(Duration.ofMillis(500)).ignoring(NoSuchElementException.class);
    element = waitWithVisiblityCheck.until(ExpectedConditions.visibilityOfElementLocated(by));
    LOGGER.info("Element {} is present in DOM with @visible='true' ", by);
    return element;
  }

  /**
   * Gets the visible element.
   * 
   * @param by - By - The location of the element that's on the UI with the visible value set to
   *        true.
   * @return WebElement - Web element if it's found. (Exception will be thrown if it's not found)
   */
  public WebElement getElement(By by) {
    return this.getElement(by, defaultWaitTimeout);
  }

  public String getText(final By by) {
    LOGGER.info("Retrieving the text of the element {}", by);
    String text = this.getElement(by, defaultWaitTimeout).getText();
    LOGGER.info("The text of the element {} was {} and is, \"{}\"", by, FOUND, text);
    return text;
  }

  public void scrollToElement(final By by) {
    this.pause(500);
    WebElement element = getWebDriver().findElement(by);
    ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].scrollIntoView(true);",
        element);
    this.pause(500);
  }
}
