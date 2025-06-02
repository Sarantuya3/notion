package com.selenium.notion.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Base Page Object class that all other page objects will extend.
 * Contains common functionality and shared elements.
 */
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Constructor for BasePage.
     *
     * @param driver The WebDriver instance.
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 15);
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets the current page title.
     *
     * @return The page title as a string.
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Gets the current page URL.
     *
     * @return The current URL as a string.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Navigates to a specific URL.
     *
     * @param url The URL to navigate to.
     */
    public void navigateTo(String url) {
        driver.get(url);
    }
}