package com.dibtr.util;

/**
 * This class is responsible to provide logging details.
 * 
 * @author Jitendra.Satpute.
 *
 */
public class CLog {
  private static final AllureLogger LOGGER = new AllureLogger(CLog.class);
  private static final String TAG = "App";

  private CLog() {
    // Hide implicit default ctor
  }

  /**
   * This utility function is used to retrieve calling function name from StackTrace.
   * 
   * @return - String - Calling function name.
   */
  public static String getTag() {
    StringBuilder sb = new StringBuilder(4);
    try {
      StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
      sb.append(caller.getMethodName());
    } catch (Exception e) {
      LOGGER.error("Got error to featch caller method name :", e);
      sb.append(TAG);
    }
    return sb.toString();
  }

}
