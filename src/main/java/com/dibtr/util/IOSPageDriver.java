package com.dibtr.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;

/**
 * The Class PageDriver.
 */
public class IOSPageDriver {
  private static final AllureLogger LOGGER = new AllureLogger(IOSPageDriver.class);
  private IOSDriver<IOSElement> iosDriver;
  private static final String ELEMENT_FOUND = "Element found.";
  private static final String ELEMENT_NOT_FOUND = "Element not found.";
  private static final String PERFORM_MESSAGE = "Trying to perform : {}";
  private static final String LOG_FORMAT = "{} : {}";
  private static final String ELEMENT = "element";
  private static final String DIRECTION = "direction";
  private static final String MOBILE_SCROLL = "mobile:scroll";
  private static final String VISIBLE = "visible";
  private int defaultWaitTimeout = 60;
  private int pollingFrequency = 500;
  public static final String BY_ELEMENT_FINDER = "element";
  public static final String BY_PIXEL = "pixel";
  public static final String BY_DIRECTION = "direction";
  public static final String DIRECTION_DOWN = "down";
  public static final String DIRECTION_UP = "up";
  private static final By XCUIElementTypeApplication = By.className("XCUIElementTypeApplication");
  private static final String FOUND = " Found: ";

  /**
   * Instantiates a new page driver.
   *
   * @param iosDriver - IOSDriver<IOSElement> - The iOS driver
   */
  public IOSPageDriver(IOSDriver<IOSElement> iosDriver) {
    this.iosDriver = iosDriver;
  }

  /**
   * Get the webDriver.
   * 
   * @return WebDriver - The web driver
   */
  public IOSDriver<IOSElement> getWebDriver() {
    return iosDriver;
  }

  /**
   * Retrieves the page source.
   * 
   * @return String - The page source in HTML format
   */
  public String getHtmlSource() {
    LOGGER.info("Retrieving page source.");
    return iosDriver.getPageSource();
  }

  /**
   * Retrieves the center point of the element.
   *
   * @param by - By - The location of the element that has the 'value' attribute
   * @return Point - Central location of the element.
   */
  public Point getElementCenterLocation(By by) {
    LOGGER.info("Retrieving the center point of the element.");
    return this.getElement(by, defaultWaitTimeout).getCenter();
  }

  /**
   * This function is used to click on Element by hitting on xCordinate & yCordinate on page.
   *
   * @param pointToTap - PointOption - Object which contains elements xCordinate & yCordinate
   *        values.
   */
  public void clickByCoordinates(PointOption<?> pointToTap) {
    LOGGER.info("Clicking by coordinates {}", pointToTap);
    new TouchAction<>(iosDriver).tap(pointToTap).perform();
  }

  /**
   * Pauses the current thread.
   * 
   * @param milliseconds - int - Time to wait in milliseconds
   */
  public void pause(final int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      LOGGER.error("ERROR: An InterruptedException has occurred in PageDriver.pause()", e);
      Thread.currentThread().interrupt();
    }
  }

  /**
   * This method is used to get Polling Interval. By default this interval is 500 milliseconds.
   *
   * @return int - Current polling interval.
   */
  public int getPollingInterval() {
    return this.pollingFrequency;
  }

  /**
   * Gets the visible element.
   *
   * @param by - By - The location of the element that's on the UI with the visible value set to
   *        true.
   * @param waitTimeOut - int - Timeout value to wait until element search query return result.
   * @return WebElement - Web element if it's found. (Exception will be thrown if it's not found)
   */
  public IOSElement getElement(By by, int waitTimeOut) {
    IOSElement element = null;
    LOGGER.info("Trying to find element with visibility check");
    Wait<WebDriver> waitWithVisiblityCheck =
        new FluentWait<WebDriver>(iosDriver).withTimeout(Duration.ofSeconds(waitTimeOut))
            .pollingEvery(Duration.ofMillis(500)).ignoring(NoSuchElementException.class);
    element = (IOSElement) (waitWithVisiblityCheck
        .until(ExpectedConditions.visibilityOfElementLocated(by)));
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
  public IOSElement getElement(By by) {
    return this.getElement(by, defaultWaitTimeout);
  }

  /**
   * To get a list of WebElements represented by a common tag.
   * 
   * @param by - By - The location of the element
   * @return List<IOSElement> - List of iOS web elements represented by a common tag
   */
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
        new FluentWait<WebDriver>(iosDriver).withTimeout(Duration.ofSeconds(timeout))
            .pollingEvery(Duration.ofMillis(500)).ignoring(NoSuchElementException.class);
    return wait.until((WebDriver driver) -> driver.findElements(by));
  }

  /**
   * Indicates whether or not the element specified is present (whether or not the element is
   * visible).
   * 
   * @param by - By - The location of the element to check
   * @return boolean - 'true' if the element is present, 'false' if it's not
   */
  public boolean isElementPresent(final By by) {
    return isElementPresent(by, defaultWaitTimeout);
  }

  /**
   * Indicates whether or not the element specified is present.
   *
   * @param by - By - The location of the element to check
   * @param timeout - int - The amount of time, in seconds, before timing out
   * @return booelean - 'true' if the element is present, 'false', if it's not
   */
  public boolean isElementPresent(By by, int timeout) {
    LOGGER.info(PERFORM_MESSAGE, CLog.getTag());
    LOGGER.info("Checking if {} is present.", by);
    boolean isElementPresent = false;
    WebElement element = null;
    try {
      element = this.getElement(by, timeout);
      isElementPresent = element.isDisplayed();
    } catch (Exception e) {
      LOGGER.error("ERROR: An exception occurred in PageDriver.isElementPresent()", e);
    }
    LOGGER.info("{} present: {}", by, isElementPresent);
    return isElementPresent;
  }

  /**
   * Clicks the specified element.
   * 
   * @param by - By - The location of the element to be clicked
   */
  public void clickElement(final By by) {
    clickElement(by, defaultWaitTimeout);
  }

  /**
   * Clicks the specified element and allows the timeout period to be specified.
   * 
   * @param by - By - The location of the element to be clicked
   * @param timeout - int - The amount of time, in seconds, to wait for the element before moving on
   */
  public void clickElement(final By by, final int timeout) {
    LOGGER.info(PERFORM_MESSAGE, CLog.getTag());
    LOGGER.info("Trying to click on {}.", by);
    this.getElement(by, timeout).click();
    LOGGER.info("Successfully Clicked on {}.", by);
  }

  public void tapElement(final By by) {
    LOGGER.info(PERFORM_MESSAGE, CLog.getTag());
    LOGGER.info("Trying to click on {}.", by);
    new TouchAction((getWebDriver())).tap(TapOptions.tapOptions()
        .withElement(ElementOption.element((getWebDriver()).findElement(by)))).perform();
  }

  /**
   * Clicks the specified element.
   *
   * @param element - IOSElement - The element to be clicked
   */
  public void clickElement(final WebElement element) {
    clickElement(element, defaultWaitTimeout);
  }

  /**
   * Clicks the specified element and allows the timeout period to be specified.
   * 
   * @param element - IOSElement - The element to be clicked
   * @param timeout - int - The amount of time, in seconds, to wait for the element before moving on
   */
  public void clickElement(final WebElement element, final int timeout) {
    LOGGER.info(PERFORM_MESSAGE, CLog.getTag());
    LOGGER.info("Trying to click on the element {}.", element);
    waitForElement(element, timeout);
    element.click();
    LOGGER.info("Clicked on the element {}", element);
  }
  
  /**
   * This method types a string character by character with a small delay between characters. This
   * is most useful for fields with auto complete lists that need time to display the auto complete
   * items.
   * 
   * @param by - By - The location of the field in which to type
   * @param value - String - The value to type
   */
  public void typeValueInFieldSlowly(final By by, final String value) {
    LOGGER.info("Trying to type {} in the {} field.", value, by);
    IOSElement iosElement;
    iosElement = this.getElement(by, defaultWaitTimeout);
    iosElement.click();
    iosElement.clear();
    for (int i = 0; i < value.length(); i++) {
      String character = value.substring(i, i + 1);
      iosElement.sendKeys(character);
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        LOGGER.error("ERROR: An InterruptedException has occurred in PageDriver.manageWindows() {}",
            e.getMessage());
        Thread.currentThread().interrupt();
      }
    }
    DriverFactory.getMobilePageDriver().hideKeyboard();
    LOGGER.info("Successfully typed {} in {} field.", value, by);
  }

  /*
   * Section 4: Swipe / Scroll screen or scrollable element -- The methods in this section interact
   * with the page by swiping in one of the four directions (up, down, left, and right) or scrolling
   * some scrollable element on the page.
   */

  /**
   * This function is used to scroll to element which is within any parent container element.
   *
   * @param parentContainer - By - Locator strategy to find Parent container.
   * @param elementToSearch - By - Locator strategy to find element & scroll to that element on
   *        page.
   * @param direction - String - In which direction want to scroll on page.
   */
  public void scrollToElementOnAppiumDevices(By parentContainer, By elementToSearch,
      String scrollingStrategy, String locatorOrDirection) {
    LOGGER.info("User selected to scroll with {} Strategy &  {} to search the element {}",
        scrollingStrategy, locatorOrDirection, elementToSearch);
    RemoteWebElement parent = null;
    RemoteWebElement elementToFind = null;
    HashMap<String, String> scrollObject = new HashMap<>();
    int count = 0;
    do {
      try {
        parent = getWebDriver().findElement(parentContainer);
        elementToFind = getWebDriver().findElement(elementToSearch);
        if (elementToFind.getAttribute(VISIBLE).equalsIgnoreCase("true")) {
          break;
        }
      } catch (Exception ex) {
        LOGGER.error("Got error while scrolling on simulator {}", ex.getMessage());
      }
      if (parent != null && scrollingStrategy.equalsIgnoreCase(BY_ELEMENT_FINDER)) {
        scrollObject.put(ELEMENT, parent.getId());
        scrollObject.put("name", locatorOrDirection);
      } else if (parent != null && scrollingStrategy.equalsIgnoreCase(BY_DIRECTION)) {
        scrollObject.put(ELEMENT, parent.getId());
        scrollObject.put(DIRECTION, locatorOrDirection);
      }
      iosDriver.executeScript(MOBILE_SCROLL, scrollObject);
      count++;
    } while (count < 1);

  }

  /*
   * Section 5: Wait for element methods -- All methods in this section relate to the wait or fluent
   * wait functions from Selenium.
   */

  /**
   * Waits for element to be available within default timeout period (60 seconds).
   *
   * @param by - By - The location of the element for which to wait
   */
  public void waitForElement(final By by) {
    waitForElement(by, defaultWaitTimeout);
  }

  /**
   * Waits for element to be available within the specified timeout period.
   * 
   * @param by - By - The location of the element for which to wait
   * @param timeout - int - The period of time to wait in seconds
   */
  public void waitForElement(final By by, final int timeout) {
    LOGGER.info(PERFORM_MESSAGE, CLog.getTag());
    LOGGER.info(">>> Waiting for element: {}", by);
    try {
      this.getElement(by, timeout);
      LOGGER.info(ELEMENT_FOUND);
    } catch (Exception e) {
      LOGGER.error(LOG_FORMAT, ELEMENT_NOT_FOUND, e);
    }
  }

  /**
   * Waits for element to be available within the default timeout period (60 seconds).
   * 
   * @param element - IOSElement - the iOS element for which to wait
   */
  public void waitForElement(IOSElement element) {
    waitForElement(element, defaultWaitTimeout);
  }

  /**
   * Waits for element to be available within the specified timeout period.
   * 
   * @param element - IOSElement - The iOS element for which to wait
   * @param timeout - int - The period of time to wait in seconds
   */
  public void waitForElement(final WebElement element, final int timeout) {
    WebDriverWait wait = new WebDriverWait(iosDriver, timeout);
    try {
      wait.until(ExpectedConditions.visibilityOf(element));
      LOGGER.info(ELEMENT_FOUND);
    } catch (Exception e) {
      LOGGER.error(LOG_FORMAT, ELEMENT_NOT_FOUND, element, e);
    }
  }

  /**
   * Captures a screenshot of the desktop/device and returns the path that it is saved to.
   * 
   * @param testCaseId - String - ID of the test case
   * @return String - Path to the screenshot
   */
  public String captureDesktop(final String testCaseId) {
    SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
    Date now = new Date();
    String strDate = sdfDate.format(now);
    String fileName = "failImage-" + testCaseId + "_" + strDate + "_.png";
    String reportsPath = "target" + File.separator + "surefire-reports" + File.separator;
    String reportTestingSuitePath =
        reportsPath + File.separator + "Report Testing Suite" + File.separator;
    String filePath = reportsPath + fileName;
    LOGGER.info("File path formed in capture desktop {} ", filePath);
    try {
      WebDriver augmentedDriver = new Augmenter().augment(getWebDriver());
      File screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
      FileUtils.copyFile(screenshot, new File(filePath));
      // This file is for Jenkins
      FileUtils.copyFile(screenshot, new File(reportTestingSuitePath + fileName));
      String imageURl = filePath.replace(reportsPath + fileName, fileName);
      Reporter.log("\r * screenshot for Test Case: " + testCaseId + " <a href='.\\" + imageURl
          + "' target='_blank' >View Image </a>"
          + "<div style='height:200px; width: 375px; overflow:scroll'><img src='./" + imageURl
          + "'></div> \r\n", true);
    } catch (Exception e) {
      LOGGER.error("An error has occured in captureDesktop()", e);
    }
    return filePath;
  }

  /**
   * Used to scroll down.
   * 
   * @param by - Element to be scrolled to
   */
  public void scrollDown(By by) {
    this.scrollDown(by, false);
  }

  /**
   * Scrolls to an element that is not currently capable of being viewed or clicked on.
   * 
   * @param by - Element to be scrolled to
   */

  /**
   * 
   * Scrolls to an element that is not currently capable of being viewed or clicked on.
   * 
   * @param by - Element to be scrolled to
   * @param scrollWithCoordinates - Will scroll with coordinate if value is 'true' else will scroll
   *        with either with 'Direction'.
   */
  public void scrollDown(By by, boolean scrollWithCoordinates) {
    if (scrollWithCoordinates) {
      this.scrollDown(by, IOSPageDriver.BY_PIXEL, null);
    } else {
      this.scrollDown(by, IOSPageDriver.BY_DIRECTION, IOSPageDriver.DIRECTION_DOWN);
    }
  }

  /**
   * Scroll to element on Simulator.
   * 
   * @param by - By - Locator strategy to find element on page.
   * @param scrollingStrategy - String - Scrolling Strategy.(Expected value - IOSPageDriver.BY_PIXEL
   *        OR BY_DIRECTION)
   * @param locatorOrDirection - String - Sub scrolling Strategy (Expected values -
   *        'IOSPageDriver.DIRECTION_DOWN' OR 'IOSPageDriver.UP' OR NAME attribute)
   */
  public void scrollDown(By by, String scrollingStrategy, String locatorOrDirection) {
    if (scrollingStrategy.equalsIgnoreCase(IOSPageDriver.BY_PIXEL)) {

    } else {
      DriverFactory.getMobilePageDriver().scrollToElementOnAppiumDevices(XCUIElementTypeApplication, by,
          scrollingStrategy, locatorOrDirection);
    }
  }

  /**
   * This function is used to Hide keyboard.
   */
  public void hideKeyboard() {
    try {
      By returnKeyOnKeyboard = By.xpath("//XCUIElementTypeButton[@name='Return']");
      DriverFactory.getMobilePageDriver().clickElement(returnKeyOnKeyboard, 10);
    } catch (Exception e) {
      System.out.println("Error while trying to hide keyboard ");
    }
  }

  public String getText(final By by) {
    LOGGER.info("Retrieving the text of the element {}", by);
    String text = this.getElement(by, defaultWaitTimeout).getText();
    LOGGER.info("The text of the element {} was {} and is, \"{}\"", by, FOUND, text);
    return text;
  }
  
  public void clickElementByLocation(final Point location) {
    LOGGER.info(PERFORM_MESSAGE, CLog.getTag());
    LOGGER.info("Trying to click at x={} y={}.", location.getX(), location.getY());
    TouchAction<?> action = new TouchAction<>(iosDriver);
    action.press(PointOption.point(location.getX(), location.getY()));
    action.waitAction(WaitOptions.waitOptions(Duration.ofMillis(pollingFrequency)));
    action.release();
    action.perform();
  }
}
