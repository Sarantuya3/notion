package com.selenium.notion.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the Notion Homepage.
 */
public class HomePage extends BasePage {

    // Login/Sign up buttons with multiple fallback selectors
    @FindBy(xpath = "//a[contains(text(), 'Log in')] | //button[contains(text(), 'Log in')] | //a[contains(@href, 'login')] | //*[contains(text(), 'Sign in')]")
    private WebElement loginButton;

    @FindBy(xpath = "//a[contains(text(), 'Sign up')] | //button[contains(text(), 'Sign up')] | //a[contains(@href, 'signup')] | //*[contains(text(), 'Get started')]")
    private WebElement signUpButton;

    // Main heading or hero text
    @FindBy(xpath = "//h1 | //h2 | //*[contains(@class, 'hero')] | //*[contains(@class, 'title')]")
    private WebElement mainHeading;

    // Navigation elements
    @FindBy(xpath = "//nav | //header | //*[contains(@class, 'nav')] | //*[contains(@class, 'header')]")
    private WebElement navigationBar;

    /**
     * Constructor for HomePage.
     *
     * @param driver The WebDriver instance.
     */
    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to the Notion homepage.
     */
    public void navigateToHomePage() {
        navigateTo("https://www.notion.so");
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOf(loginButton),
                ExpectedConditions.visibilityOf(mainHeading)
            ));
        } catch (Exception e) {
            System.out.println("Homepage elements not immediately visible, proceeding...");
        }
    }

    /**
     * Clicks the login button to navigate to the login page.
     *
     * @return LoginPage object.
     */
    public LoginPage clickLoginButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            loginButton.click();
            return new LoginPage(driver);
        } catch (Exception e) {
            System.out.println("Login button not found, trying direct navigation...");
            navigateTo("https://www.notion.so/login");
            return new LoginPage(driver);
        }
    }

    /**
     * Checks if the main heading is visible.
     *
     * @return true if the heading is visible, false otherwise.
     */
    public boolean isMainHeadingVisible() {
        try {
            return mainHeading.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the navigation bar is visible.
     *
     * @return true if the navigation is visible, false otherwise.
     */
    public boolean isNavigationVisible() {
        try {
            return navigationBar.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the text of the main heading.
     *
     * @return The heading text, or empty string if not found.
     */
    public String getMainHeadingText() {
        try {
            return mainHeading.getText();
        } catch (Exception e) {
            return "";
        }
    }
}