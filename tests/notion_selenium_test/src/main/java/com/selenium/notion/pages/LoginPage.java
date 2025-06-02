package com.selenium.notion.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the Notion Login Page.
 */
public class LoginPage extends BasePage {

    // Email/Username field with multiple selectors
    @FindBy(xpath = "//input[@type='email'] | //input[contains(@placeholder, 'email')] | //input[contains(@placeholder, 'Email')] | //input[@name='email'] | //input[@id='email']")
    private WebElement emailField;

    // Password field
    @FindBy(xpath = "//input[@type='password'] | //input[contains(@placeholder, 'password')] | //input[contains(@placeholder, 'Password')] | //input[@name='password'] | //input[@id='password']")
    private WebElement passwordField;

    // Submit/Login button
    @FindBy(xpath = "//button[contains(text(), 'Continue')] | //button[contains(text(), 'Log in')] | //button[contains(text(), 'Sign in')] | //button[@type='submit'] | //input[@type='submit']")
    private WebElement submitButton;

    // Error message element
    @FindBy(xpath = "//*[contains(text(), 'Invalid')] | //*[contains(text(), 'incorrect')] | //*[contains(text(), 'error')] | //*[contains(@class, 'error')] | //*[contains(@class, 'invalid')]")
    private WebElement errorMessage;

    /**
     * Constructor for LoginPage.
     *
     * @param driver The WebDriver instance.
     */
    public LoginPage(WebDriver driver) {
        super(driver);
        // Wait for the page to load
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOf(emailField),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='email'] | //input[@type='password']"))
            ));
        } catch (Exception e) {
            System.out.println("Login page elements not immediately visible, proceeding...");
        }
    }

    /**
     * Navigates directly to the login page.
     */
    public void navigateToLoginPage() {
        navigateTo("https://www.notion.so/login");
        try {
            Thread.sleep(2000); // Wait for page load
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Enters email in the email field.
     *
     * @param email The email to enter.
     */
    public void enterEmail(String email) {
        try {
            wait.until(ExpectedConditions.visibilityOf(emailField));
            emailField.clear();
            emailField.sendKeys(email);
        } catch (Exception e) {
            System.out.println("Email field not found with primary selector, trying alternatives...");
            // Try alternative selectors
            try {
                WebElement altEmailField = driver.findElement(By.xpath("//input"));
                altEmailField.clear();
                altEmailField.sendKeys(email);
            } catch (Exception ex) {
                System.out.println("Could not locate email field: " + ex.getMessage());
            }
        }
    }

    /**
     * Enters password in the password field.
     *
     * @param password The password to enter.
     */
    public void enterPassword(String password) {
        try {
            wait.until(ExpectedConditions.visibilityOf(passwordField));
            passwordField.clear();
            passwordField.sendKeys(password);
        } catch (Exception e) {
            System.out.println("Password field not found: " + e.getMessage());
        }
    }

    /**
     * Clicks the submit/login button.
     */
    public void clickSubmitButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(submitButton));
            submitButton.click();
        } catch (Exception e) {
            System.out.println("Submit button not found with primary selector, trying alternatives...");
            try {
                WebElement altSubmit = driver.findElement(By.xpath("//button | //input[@type='submit']"));
                altSubmit.click();
            } catch (Exception ex) {
                System.out.println("Could not locate submit button: " + ex.getMessage());
            }
        }
    }

    /**
     * Performs a complete login with email and password.
     *
     * @param email The email to use for login.
     * @param password The password to use for login.
     * @return DashboardPage object if login successful.
     */
    public DashboardPage login(String email, String password) {
        enterEmail(email);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        enterPassword(password);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        clickSubmitButton();
        
        return new DashboardPage(driver);
    }

    /**
     * Checks if an error message is visible.
     *
     * @return true if error message is visible, false otherwise.
     */
    public boolean isErrorMessageVisible() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the error message text.
     *
     * @return The error message text.
     */
    public String getErrorMessageText() {
        try {
            return errorMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }
}