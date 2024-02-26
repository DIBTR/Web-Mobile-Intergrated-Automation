package com.dibtr.util;

import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllureLogger {
  private Logger logger;

  /**
   * Constructor
   *
   * @param clazz class to log against
   */
  public AllureLogger(Class<?> clazz) {
    logger = LoggerFactory.getLogger(clazz);
  }

  /**
   * Method which takes a string containing {} and sequentially replaces it with the supplied args
   *
   * @param logMsg the string to replace {} in
   * @param args the values to replace {} with
   * @return the reconstructed string
   */
  private String replaceArguments(String logMsg, Object... args) {
    String logString = logMsg;
    for (int i = 0; i < args.length; i++) {
      logString = logString.replaceFirst("\\{}", args[i].toString());
    }
    return logString;
  }

  /**
   * Trace level log
   *
   * @param logMsg value to log
   */
  public void trace(String logMsg) {
    if (logger.isTraceEnabled()) {
      Allure.step(logMsg);
      logger.trace(logMsg);
    }
  }

  /**
   * Trace level log
   *
   * @param logMsg value to log
   * @param arg value to insert
   */
  public void trace(String logMsg, Object arg) {
    if (logger.isTraceEnabled()) {
      Allure.step(logMsg.replaceFirst("\\{}", arg.toString()));
      logger.trace(logMsg, arg);
    }
  }

  /**
   * Trace level log
   *
   * @param logMsg value to log
   * @param args values to insert
   */
  public void trace(String logMsg, Object... args) {
    if (logger.isTraceEnabled()) {
      Allure.step(replaceArguments(logMsg, args));
      logger.info(logMsg, args);
    }
  }

  /**
   * Info level log
   *
   * @param logMsg value to log
   */
  public void info(String logMsg) {
    if (logger.isInfoEnabled()) {
      Allure.step(logMsg);
      logger.info(logMsg);
    }
  }

  /**
   * Info level log
   *
   * @param logMsg value to log
   * @param arg value to insert
   */
  public void info(String logMsg, Object arg) {
    if (logger.isInfoEnabled()) {
      Allure.step(logMsg.replaceFirst("\\{}", arg.toString()));
      logger.info(logMsg, arg);
    }
  }

  /**
   * Info level log
   *
   * @param logMsg value to log
   * @param args values to insert
   */
  public void info(String logMsg, Object... args) {
    if (logger.isInfoEnabled()) {
      Allure.step(replaceArguments(logMsg, args));
      logger.info(logMsg, args);
    }
  }

  /**
   * Warn level log
   *
   * @param logMsg value to log
   */
  public void warn(String logMsg) {
    if (logger.isWarnEnabled()) {
      Allure.step(logMsg);
      logger.warn(logMsg);
    }
  }

  /**
   * Warn level log
   *
   * @param logMsg value to log
   * @param arg value to insert
   */
  public void warn(String logMsg, Object arg) {
    if (logger.isWarnEnabled()) {
      Allure.step(logMsg.replaceFirst("\\{}", arg.toString()));
      logger.warn(logMsg, arg);
    }
  }

  /**
   * Warn level log
   *
   * @param logMsg value to log
   * @param args values to insert
   */
  public void warn(String logMsg, Object... args) {
    if (logger.isWarnEnabled()) {
      Allure.step(replaceArguments(logMsg, args));
      logger.warn(logMsg, args);
    }
  }

  /**
   * Error level log
   *
   * @param logMsg value to log
   */
  public void error(String logMsg) {
    if (logger.isErrorEnabled()) {
      Allure.step(logMsg);
      logger.error(logMsg);
    }
  }

  /**
   * Error level log
   *
   * @param logMsg value to log
   * @param arg value to insert
   */
  public void error(String logMsg, Object arg) {
    if (logger.isErrorEnabled()) {
      Allure.step(logMsg.replaceFirst("\\{}", arg.toString()));
      logger.error(logMsg, arg);
    }
  }

  /**
   * Error level log
   *
   * @param logMsg value to log
   * @param args values to insert
   */
  public void error(String logMsg, Object... args) {
    if (logger.isErrorEnabled()) {
      Allure.step(replaceArguments(logMsg, args));
      logger.error(logMsg, args);
    }
  }
}
