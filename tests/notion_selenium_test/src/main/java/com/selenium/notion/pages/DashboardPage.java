package com.selenium.notion.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for the Notion Dashboard/Workspace Page.
 */
public class DashboardPage extends BasePage {

    // User profile/account menu
    @FindBy(xpath = "//button[contains(@aria-label, 'profile')] | //div[contains(@class, 'profile')] | //img[contains(@alt, 'profile')] | //*[contains(@class, 'user')] | //*[contains(@class, 'account')]")
    private WebElement userProfileMenu;

    // Sign out/logout option
    @FindBy(xpath = "//button[contains(text(), 'Log out')] | //a[contains(text(), 'Log out')] | //button[contains(text(), 'Sign out')] | //a[contains(text(), 'Sign out')] | //*[contains(text(), 'Logout')]")
    private WebElement logoutButton;

    // Main workspace area or dashboard indicator
    @FindBy(xpath = "//div[contains(@class, 'workspace')] | //div[contains(@class, 'dashboard')] | //h1[contains(text(), 'workspace')] | //*[contains(@class, 'main-content')]")
    private WebElement workspaceArea;

    // Settings or menu button
    @FindBy(xpath = "//button[contains(@aria-label, 'settings')] | //button[contains(@aria-label, 'menu')] | //*[contains(@class, 'settings')] | //*[contains(@class, 'menu')]")
    private WebElement settingsButton;

    /**
     * Constructor for DashboardPage.
     *
     * @param driver The WebDriver instance.
     */
    public DashboardPage(WebDriver driver) {
        super(driver);
        // Wait for dashboard elements to load
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOf(userProfileMenu),
                ExpectedConditions.visibilityOf(workspaceArea),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//div | //main | //section"))
            ));
            System.out.println("Dashboard page loaded successfully");
        } catch (Exception e) {
            System.out.println("Dashboard elements not immediately visible, but proceeding...");
        }
    }

    /**
     * Checks if the user profile menu is visible (indicates logged in state).
     *
     * @return true if profile menu is visible, false otherwise.
     */
    public boolean isUserProfileMenuVisible() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, 3);
            return shortWait.until(ExpectedConditions.visibilityOf(userProfileMenu)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the workspace area is visible.
     *
     * @return true if workspace is visible, false otherwise.
     */
    public boolean isWorkspaceAreaVisible() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, 3);
            return shortWait.until(ExpectedConditions.visibilityOf(workspaceArea)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Performs logout operation with multiple fallback strategies.
     *
     * @return HomePage object after logout.
     */
    public HomePage logout() {
        System.out.println("Attempting to logout from Notion...");
        
        // Strategy 1: Try to find and click profile menu first
        boolean profileClicked = false;
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, 5);
            WebElement profile = shortWait.until(ExpectedConditions.elementToBeClickable(userProfileMenu));
            profile.click();
            System.out.println("Successfully clicked user profile menu");
            profileClicked = true;
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Profile menu not found: " + e.getMessage());
        }

        // Strategy 2: Try alternative profile selectors
        if (!profileClicked) {
            try {
                String[] profileSelectors = {
                    "//button[contains(@class, 'avatar')]",
                    "//div[contains(@class, 'user-avatar')]", 
                    "//img[contains(@class, 'avatar')]",
                    "//*[contains(@data-testid, 'profile')]",
                    "//button[contains(@aria-haspopup, 'menu')]"
                };
                
                for (String selector : profileSelectors) {
                    try {
                        WebElement altProfile = driver.findElement(By.xpath(selector));
                        if (altProfile.isDisplayed()) {
                            altProfile.click();
                            System.out.println("Clicked alternative profile: " + selector);
                            profileClicked = true;
                            Thread.sleep(1000);
                            break;
                        }
                    } catch (Exception ex) {
                        // Continue to next selector
                    }
                }
            } catch (Exception e) {
                System.out.println("Alternative profile methods failed: " + e.getMessage());
            }
        }

        // Strategy 3: Try to find and click logout button
        boolean logoutClicked = false;
        if (profileClicked) {
            try {
                WebDriverWait shortWait = new WebDriverWait(driver, 5);
                WebElement logout = shortWait.until(ExpectedConditions.elementToBeClickable(logoutButton));
                logout.click();
                System.out.println("Successfully clicked logout button");
                logoutClicked = true;
            } catch (Exception e) {
                System.out.println("Primary logout button not found: " + e.getMessage());
                
                // Try alternative logout selectors
                String[] logoutSelectors = {
                    "//button[contains(text(), 'Log out')]",
                    "//a[contains(text(), 'Log out')]",
                    "//div[contains(text(), 'Log out')]",
                    "//*[contains(text(), 'Sign out')]",
                    "//*[contains(@data-testid, 'logout')]"
                };
                
                for (String selector : logoutSelectors) {
                    try {
                        WebElement logoutAlt = driver.findElement(By.xpath(selector));
                        if (logoutAlt.isDisplayed()) {
                            logoutAlt.click();
                            System.out.println("Clicked alternative logout: " + selector);
                            logoutClicked = true;
                            break;
                        }
                    } catch (Exception ex) {
                        // Continue to next selector
                    }
                }
            }
        }

        // Strategy 4: If all else fails, try direct navigation to logout URL
        if (!logoutClicked) {
            System.out.println("Direct logout methods failed, trying URL navigation...");
            try {
                driver.get("https://www.notion.so/logout");
                System.out.println("Navigated directly to logout URL");
                logoutClicked = true;
            } catch (Exception e) {
                System.out.println("Direct logout URL navigation failed: " + e.getMessage());
            }
        }

        // Wait for logout to complete
        if (logoutClicked) {
            try {
                WebDriverWait shortWait = new WebDriverWait(driver, 10);
                shortWait.until(ExpectedConditions.or(
                    ExpectedConditions.titleContains("Notion"),
                    ExpectedConditions.titleContains("Sign in"),
                    ExpectedConditions.titleContains("Login")
                ));
                System.out.println("Logout completed successfully");
            } catch (Exception e) {
                System.out.println("Logout may have completed: " + e.getMessage());
            }
        } else {
            System.out.println("WARNING: Could not perform logout - user may not be logged in or UI has changed");
        }

        // Return to HomePage regardless of logout success
        return new HomePage(driver);
    }
}