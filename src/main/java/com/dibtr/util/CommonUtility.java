package com.dibtr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

public class CommonUtility {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtility.class);
  private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private int mCount = 0;

  public static String getAnalyticsFilterData(String fullAnalyticsData) {
    ArrayList<String> onlySentRequests = null;
    String filteredData = null;
    try {
      onlySentRequests = new ArrayList<>();
      LOGGER.info("Received data from getAnalyticsFilterData ");
      // Take raw analytics data from logcat to process & separate it by space character.
      String[] aftersplit = fullAnalyticsData.split("\\s");

      // Here filter space separate data & consider only those data which contains Sent request
      // information.
      int i = 0;
      for (String strLocal : aftersplit) {
        if (aftersplit[i].contains("Sent")) {
          onlySentRequests.add(strLocal.substring(5, strLocal.length() - 1));
        }
        i++;
      }
    } catch (Exception e) {
      LOGGER.error("Got exception while processing getAnalyticsFilterData ", e);
    }
    if (onlySentRequests != null) {
      LOGGER.info("Analytics Filter Data {} ", onlySentRequests.get(onlySentRequests.size() - 1));
      filteredData = onlySentRequests.get(onlySentRequests.size() - 1);
    }
    return filteredData;
  }


  /**
   * This method will return the random alphabetic string with the length passed as parameter.
   * 
   * @Param length
   * @returns result
   */
  public static String getRandomAlphabeticString(int length) {
    StringBuilder randStr = new StringBuilder();
    for (int i = 0; i < length; i++) {
      int number = getRandomNumber();
      char ch = CHAR_LIST.charAt(number);
      randStr.append(ch);
    }
    return randStr.toString();
  }

  /**
   * This method will return the random Numeric string with the length passed as parameter.
   * 
   * @Param length - int - Length for which want to generate random number string.
   * @returns result - Random number string.
   */
  public static String getRandomNumericString(int charLength) {
    ArrayList<Integer> list = new ArrayList<>();
    int randomNumber = 0;
    int low = 2;
    int high = 9;
    for (int i = 0; i <= charLength; i++) {
      randomNumber = new Random().nextInt(high - low) + low;
      list.add(randomNumber);
    }
    return convertToString(list);
  }

  /**
   * This function is used to convert arraylist to String.
   * 
   * @param numbers - Arraylist<Integer> - Random number list to convert into string
   * @return - random number string.
   */
  private static String convertToString(ArrayList<Integer> numbers) {
    StringBuilder builder = new StringBuilder();
    for (int number : numbers) {
      builder.append(number);
    }
    builder.setLength(builder.length() - 1);
    return builder.toString();
  }

  /**
   * This method generates random numbers
   * 
   * @return int - random number.
   */
  private static int getRandomNumber() {
    int randomInt = 0;
    try {
      Random rand = SecureRandom.getInstanceStrong();
      randomInt = rand.nextInt(CHAR_LIST.length());
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("Got exception while performing getRandomNumber()", e);
    }
    if (randomInt - 1 == -1) {
      return randomInt;
    } else {
      return randomInt - 1;
    }
  }

  /**
   * This utility function is used to get random number within specified range.
   * 
   * @return - Random number between value 15 to 25.
   */
  public static int getRandomNumberWithinRange() {
    return getRandomNumberWithinRange(15, 25);
  }

  /**
   * This utility function is used to get random number within specified range.
   * 
   * @param startRange - int - Start point of range.
   * @param endRange - int - End point of range.
   * @return - Random number within user specified range.
   */
  public static int getRandomNumberWithinRange(int startRange, int endRange) {
    int rand = startRange;
    while (true) {
      rand = new Random().nextInt(endRange);
      if (rand > startRange)
        break;
    }
    return rand;
  }

  /**
   * Capitalize the first letter of each word
   * 
   * @param line
   * @return
   */
  public static String initCap(String string) {
    char[] chars = string.toLowerCase().toCharArray();
    boolean found = false;
    for (int i = 0; i < chars.length; i++) {
      if (!found && Character.isLetter(chars[i])) {
        chars[i] = Character.toUpperCase(chars[i]);
        found = true;
      } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') {
        found = false;
      }
    }
    return String.valueOf(chars);
  }

  /**
   * Gets a <code>Calendar</code> and initializes it.
   *
   * @param date The date for which to set the <code>calendar</code>.
   *
   * @return An initialized <code>Calendar</code>.
   *
   * @throws IllegalArgumentException An illegal or inappropriate argument was passed.
   */
  private static Calendar initializeCalendar(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setLenient(false);
    if (date == null)
      throw new IllegalArgumentException("Invalid date.");
    cal.setTime(date);
    return cal;
  }

  /**
   * Clears the time portion of the given date. Though the time portion of a date can not be
   * completely removed, the time is set to all zeros, the default time which stands for midnight
   * (12:00:00 am).
   *
   * @param date The date for which to remove the time stamp.
   *
   * @return The given date with the time portion removed, thus all zeros.
   */
  public static Date clearTime(Date date) {
    Calendar cal = initializeCalendar(date);

    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.clear(Calendar.MINUTE);
    cal.clear(Calendar.SECOND);
    cal.clear(Calendar.MILLISECOND);

    return cal.getTime();
  }

  /**
   * Returns a date based on the requested format and the requested days out from todays system
   * date.
   * 
   * @param days
   * @param format
   * @return
   */
  public static String getDate(int days, String format) {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    Calendar cal = initializeCalendar(calendar.getTime());
    // Do the math.
    cal.add(Calendar.DAY_OF_MONTH, days);
    // Format the date to the desired format and return.
    return dateFormat.format(clearTime(cal.getTime()));
  }

  public Date getDate(int days) {
    Calendar calendar = Calendar.getInstance();
    Calendar cal = initializeCalendar(calendar.getTime());
    // Do the math.
    cal.add(Calendar.DAY_OF_MONTH, days);

    return clearTime(cal.getTime());
  }


  public static String getFormattedDate(String oldFormat, String newFormat, String oldDateString) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(oldFormat);
    Date date = null;
    try {
      date = simpleDateFormat.parse(oldDateString);
    } catch (ParseException e) {
      LOGGER.info("parse exception");
      e.printStackTrace();
    }
    simpleDateFormat.applyPattern(newFormat);
    return simpleDateFormat.format(date);
  }

  /**
   * Load properties from the local properties file
   * 
   * @return
   */
  public static Properties loadProperties() {
    Properties testParams = new Properties();
    try (InputStream input = new FileInputStream("config.properties")) {
      testParams.load(input);
    } catch (Exception ex) {
      LOGGER.error("Got exception while performing loadProperties() ", ex);
    }
    updateProperties(testParams);
    return testParams;
  }

  /**
   * Update current properties with any found in the corresponding environment variables from the
   * maven command line options.
   */
  private static void updateProperties(Properties testParams) {
    Enumeration<?> e = testParams.keys();
    String currentPropValue = "";
    String currentEnvValue = "";
    String currentKey = "";
    while (e.hasMoreElements()) {
      currentKey = (String) e.nextElement();
      currentPropValue = System.getProperty(currentKey);
      currentEnvValue = System.getenv(currentKey);
      if (currentPropValue != null) {
        testParams.setProperty(currentKey, currentPropValue);
      }
      if (currentEnvValue != null && currentKey.equals("TUNNEL_IDENTIFIER")) {
        testParams.setProperty(currentKey, currentEnvValue);
      }
      LOGGER.info("{} -- {} ", currentKey, testParams.getProperty(currentKey));
    }
  }

  /***
   * captureDesktop
   * 
   * @return
   ***/
  public String captureDesktop(String testCaseId, WebDriver driver) {
    SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
    Date now = new Date();
    String strDate = sdfDate.format(now);
    String fileName = "failImage-" + testCaseId + "_" + strDate + "_.png";
    String filePath = "target//surefire-reports//" + fileName;

    try {
      WebDriver augmentedDriver = new Augmenter().augment(driver);

      File screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
      FileUtils.copyFile(screenshot, new File(filePath));
      // This file is for Jenkins
      FileUtils.copyFile(screenshot,
          new File("target//surefire-reports//Report Testing Suite//" + fileName));

      String imageURl = filePath.replace("target//surefire-reports//" + fileName, fileName);
      Reporter.log("\r * screenshot for Test Case: " + testCaseId + " <a href='.//" + imageURl
          + "' target='_blank' >View Image </a>"
          + "<div style='height:200px; width: 375px; overflow:scroll'><img src='./" + imageURl
          + "'></div> \r\n", true);
    } catch (RuntimeException ex) {
      LOGGER.error("Got RuntimeException while performing captureDesktop() ", ex);
    } catch (Exception e) {
      LOGGER.error("Got exception while performing captureDesktop() ", e);
    }
    return filePath;
  }

  /**
   * Log to console
   * 
   * @param string
   */
  void logToConsole(String string) {
    LOGGER.info(string);
    if (++mCount % 40 == 0) {
      LOGGER.info("");
    }
  }

  /**
   * Do http post
   * 
   * @param url
   * @param urlParameters
   * @return
   */
  public String doPost(String url, List<NameValuePair> urlParameters) {
    StringBuilder result = new StringBuilder();
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      HttpPost httppost = new HttpPost(url);
      // add header
      httppost.setHeader("User-Agent", "Mozilla/5.0");
      httppost.setEntity(new UrlEncodedFormEntity(urlParameters));
      HttpResponse response = httpclient.execute(httppost);
      BufferedReader rd =
          new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      String line = "";
      while ((line = rd.readLine()) != null) {
        result.append(line);
      }
    } catch (Exception e) {
      LOGGER.error("Got exception while performing doPost() ", e);
    }
    return result.toString();
  }

  /**
   * Do http post with variable content types.
   * 
   * @param url
   * @param builder
   */
  public void doPost(String url, MultipartEntityBuilder builder) {
    StringBuilder result = new StringBuilder();
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      HttpPost httppost = new HttpPost(url);
      httppost.setHeader("User-Agent", "Mozilla/5.0");
      HttpEntity entity = builder.build();
      httppost.setEntity(entity);
      HttpResponse response = httpclient.execute(httppost);
      BufferedReader rd =
          new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      String line = "";
      while ((line = rd.readLine()) != null) {
        result.append(line);
      }
    } catch (Exception e) {
      LOGGER.error("Got exception while performing do post ", e);
    }
  }
}
