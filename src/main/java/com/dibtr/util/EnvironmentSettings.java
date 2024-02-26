package com.dibtr.util;

import java.util.Properties;


public class EnvironmentSettings {
  private static EnvironmentSettings settings;
  private Properties properties;

  /**
   * Constructor, instantiate class with default properties file.
   */
  private EnvironmentSettings() {
    this.properties = CommonUtility.loadProperties();
  }

  
  /**
   * Get environment settings using default properties files.
   * 
   * @return EnvironmentSettings
   */
  public static EnvironmentSettings getInstance() {
    if (settings == null) {
      settings = new EnvironmentSettings();
    }
    return settings;
  }

  /**
   * Get environment settings using properties file specified.
   * 
   * @param filePath is path to properties file to be used.
   * @return EnvironmentSettings
   */
  public static EnvironmentSettings getInstance(final String filePath) {
    if (settings == null) {
      settings = new EnvironmentSettings();
    }
    return settings;
  }

  /**
   * Get Properties object that contains environment settings.
   * 
   * @return the properties
   */
  public Properties getProperties() {
    return properties;
  }

  /**
   * Get specific property from the environment settings.
   * 
   * @param key is the name of the property to retrieve.
   * @return property value.
   */
  public String getProperty(final String key) {
    return properties.getProperty(key);
  }

  /**
   * Get specific property from the environment settings. It will return defaultValue if value for
   * given key is null.
   * 
   * @param key is the name of the property to retrieve.
   * @param defaultValue is value returned if value for key is null.
   * @return property value.
   */
  public String getProperty(final String key, final String defaultValue) {
    return (properties.getProperty(key) != null) ? properties.getProperty(key) : defaultValue;
  }

  /**
   * Set a configuration property using the given key/value pair.
   * 
   * @param key - the configuration key
   * @param value - the configuration value
   */
  public void setProperty(final String key, final String value) {
    properties.setProperty(key, value);
  }
}
