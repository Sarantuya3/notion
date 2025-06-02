package com.selenium.notion.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the Notion About Page.
 */
public class AboutPage extends BasePage {

    // Main heading
    @FindBy(xpath = "//h1 | //h2[contains(text(), 'About')] | //*[contains(@class, 'heading')] | //*[contains(@class, 'title')]")
    private WebElement mainHeading;

    // Any content area
    @FindBy(xpath = "//main | //section | //article | //div[contains(@class, 'content')] | //div[contains(@class, 'about')]")
    private WebElement contentArea;

    // Navigation or back button
    @FindBy(xpath = "//nav | //header | //*[contains(@class, 'nav')] | //a[contains(@href, '/')]")
    private WebElement navigationArea;

    /**
     * Constructor for AboutPage.
     *
     * @param driver The WebDriver instance.
     */
    public AboutPage(WebDriver driver) {
        super(driver);
        // Wait for page elements to load
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOf(mainHeading),
                ExpectedConditions.visibilityOf(contentArea)
            ));
        } catch (Exception e) {
            System.out.println("About page elements not immediately visible, proceeding...");
        }
    }

    /**
     * Navigates to the About page.
     */
    public void navigateToAboutPage() {
        navigateTo("https://www.notion.so/about");
        try {
            Thread.sleep(2000); // Wait for page load
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Checks if the main heading is visible.
     *
     * @return true if heading is visible, false otherwise.
     */
    public boolean isMainHeadingVisible() {
        try {
            return mainHeading.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the main heading text.
     *
     * @return The heading text.
     */
    public String getMainHeadingText() {
        try {
            return mainHeading.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Checks if the content area is visible.
     *
     * @return true if content is visible, false otherwise.
     */
    public boolean isContentAreaVisible() {
        try {
            return contentArea.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if navigation is visible.
     *
     * @return true if navigation is visible, false otherwise.
     */
    public boolean isNavigationVisible() {
        try {
            return navigationArea.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}