package com.selenium.notion.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for reading configuration properties from config.properties file.
 */
public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";

    static {
        loadProperties();
    }

    /**
     * Loads properties from the config file.
     */
    private static void loadProperties() {
        try {
            properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Configuration file not found at " + CONFIG_FILE_PATH);
        }
    }

    /**
     * Gets a property value by key.
     *
     * @param key The property key.
     * @return The property value.
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets the base URL for the application.
     *
     * @return The base URL.
     */
    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    /**
     * Gets the login URL.
     *
     * @return The login URL.
     */
    public static String getLoginUrl() {
        return getProperty("login.url");
    }

    /**
     * Gets the about URL.
     *
     * @return The about URL.
     */
    public static String getAboutUrl() {
        return getProperty("about.url");
    }

    /**
     * Gets the test username.
     *
     * @return The test username.
     */
    public static String getTestUsername() {
        return getProperty("test.username");
    }

    /**
     * Gets the test password.
     *
     * @return The test password.
     */
    public static String getTestPassword() {
        return getProperty("test.password");
    }

    /**
     * Gets the real test username for actual login testing.
     *
     * @return The real test username.
     */
    public static String getRealTestUsername() {
        return getProperty("real.test.username");
    }

    /**
     * Gets the real test password for actual login testing.
     *
     * @return The real test password.
     */
    public static String getRealTestPassword() {
        return getProperty("real.test.password");
    }

    /**
     * Gets the implicit wait timeout in seconds.
     *
     * @return The implicit wait timeout.
     */
    public static int getImplicitWaitSeconds() {
        return Integer.parseInt(getProperty("implicit.wait.seconds"));
    }

    /**
     * Gets the explicit wait timeout in seconds.
     *
     * @return The explicit wait timeout.
     */
    public static int getExplicitWaitSeconds() {
        return Integer.parseInt(getProperty("explicit.wait.seconds"));
    }

    /**
     * Gets the expected page title for homepage.
     *
     * @return The expected homepage title.
     */
    public static String getExpectedHomeTitle() {
        return getProperty("expected.home.title");
    }

    /**
     * Gets the expected page title for login page.
     *
     * @return The expected login page title.
     */
    public static String getExpectedLoginTitle() {
        return getProperty("expected.login.title");
    }
}