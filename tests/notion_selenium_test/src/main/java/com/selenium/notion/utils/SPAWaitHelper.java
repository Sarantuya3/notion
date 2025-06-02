package com.selenium.notion.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Helper class for waiting on SPA (Single Page Application) elements to load.
 * Specifically designed for modern React/Vue applications like Notion.
 */
public class SPAWaitHelper {
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor jsExecutor;
    
    public SPAWaitHelper(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeoutSeconds);
        this.jsExecutor = (JavascriptExecutor) driver;
    }
    
    /**
     * Waits for the page to be fully loaded including JavaScript execution.
     */
    public void waitForPageToLoad() {
        System.out.println("Waiting for page to fully load...");
        
        // Wait for document ready state
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return jsExecutor.executeScript("return document.readyState").equals("complete");
            }
        });
        
        // Wait for jQuery if present
        try {
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return (Boolean) jsExecutor.executeScript("return typeof jQuery === 'undefined' || jQuery.active === 0");
                }
            });
        } catch (Exception e) {
            // jQuery not present, continue
        }
        
        System.out.println("Page loading completed");
    }
    
    /**
     * Waits for React components to finish loading.
     */
    public void waitForReactToLoad() {
        System.out.println("Waiting for React components to load...");
        
        try {
            // Wait for React to be available
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return (Boolean) jsExecutor.executeScript(
                        "return typeof React !== 'undefined' || " +
                        "typeof window.React !== 'undefined' || " +
                        "document.querySelector('[data-reactroot]') !== null || " +
                        "document.querySelector('[data-react-helmet]') !== null"
                    );
                }
            });
            
            // Additional wait for React components to render
            Thread.sleep(2000);
            System.out.println("React components loaded");
            
        } catch (Exception e) {
            System.out.println("React not detected or timeout, continuing...");
        }
    }
    
    /**
     * Waits for dynamic form elements to appear in a modern SPA.
     * Uses progressive waiting with increasing delays.
     */
    public void waitForDynamicContent(int maxWaitSeconds) {
        System.out.println("Waiting for dynamic content to load...");
        
        int attempts = 0;
        int maxAttempts = maxWaitSeconds / 2; // Check every 2 seconds
        
        while (attempts < maxAttempts) {
            try {
                // Check if any form elements have appeared
                Boolean hasFormElements = (Boolean) jsExecutor.executeScript(
                    "return document.querySelectorAll('input, button, form').length > 0"
                );
                
                if (hasFormElements) {
                    System.out.println("Dynamic form elements detected!");
                    Thread.sleep(1000); // Small additional wait for stability
                    return;
                }
                
                // Check for loading indicators
                Boolean hasLoadingIndicators = (Boolean) jsExecutor.executeScript(
                    "return document.querySelectorAll('[class*=\"loading\"], [class*=\"spinner\"]').length > 0"
                );
                
                if (hasLoadingIndicators) {
                    System.out.println("Loading indicators detected, waiting for completion...");
                }
                
                // Wait for network activity to settle
                Boolean networkIdle = (Boolean) jsExecutor.executeScript(
                    "return typeof window.fetch === 'undefined' || " +
                    "window.performance.getEntriesByType('resource').filter(r => r.responseEnd === 0).length === 0"
                );
                
                if (!networkIdle) {
                    System.out.println("Network activity detected, waiting...");
                }
                
                attempts++;
                Thread.sleep(2000);
                
            } catch (Exception e) {
                System.out.println("Error checking dynamic content: " + e.getMessage());
                attempts++;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        System.out.println("Dynamic content wait completed after " + (attempts * 2) + " seconds");
    }
    
    /**
     * Waits for a specific element using multiple strategies.
     */
    public WebElement waitForElementWithMultipleStrategies(String[] xpaths, String description) {
        System.out.println("Searching for " + description + " using multiple strategies...");
        
        // First, wait for page to be stable
        waitForPageToLoad();
        waitForReactToLoad();
        
        // Try each xpath strategy
        for (int attempt = 0; attempt < 3; attempt++) {
            System.out.println("Attempt " + (attempt + 1) + " to find " + description);
            
            for (String xpath : xpaths) {
                try {
                    WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
                    if (element.isDisplayed()) {
                        System.out.println("Found " + description + " with xpath: " + xpath);
                        return element;
                    }
                } catch (Exception e) {
                    // Continue to next strategy
                }
            }
            
            // Wait a bit before next attempt
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("Could not find " + description + " with any strategy");
        return null;
    }
    
    /**
     * Waits for any input field to appear on the page.
     */
    public WebElement waitForAnyInputField() {
        String[] inputXpaths = {
            "//input[@type='email']",
            "//input[contains(@placeholder, 'email')]",
            "//input[contains(@placeholder, 'Email')]", 
            "//input[contains(@name, 'email')]",
            "//input[contains(@id, 'email')]",
            "//input[@type='text']",
            "//input",
            "//*[@contenteditable='true']", // For modern SPAs that use contenteditable
            "//div[contains(@class, 'input')]//input",
            "//form//input"
        };
        
        return waitForElementWithMultipleStrategies(inputXpaths, "email input field");
    }
    
    /**
     * Waits for any submit/continue button to appear.
     */
    public WebElement waitForSubmitButton() {
        String[] buttonXpaths = {
            "//button[contains(text(), 'Continue')]",
            "//button[contains(text(), 'Continue with email')]",
            "//button[contains(text(), 'Log in')]",
            "//button[contains(text(), 'Sign in')]",
            "//button[@type='submit']",
            "//input[@type='submit']",
            "//button[contains(@class, 'submit')]",
            "//button[contains(@class, 'primary')]",
            "//button",
            "//div[contains(@role, 'button')]",
            "//*[contains(@onclick, 'submit')]"
        };
        
        return waitForElementWithMultipleStrategies(buttonXpaths, "submit button");
    }
    
    /**
     * Waits for page URL to change (useful for SPAs).
     */
    public boolean waitForUrlToChange(String currentUrl, int timeoutSeconds) {
        System.out.println("Waiting for URL to change from: " + currentUrl);
        
        try {
            WebDriverWait urlWait = new WebDriverWait(driver, timeoutSeconds);
            return urlWait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return !driver.getCurrentUrl().equals(currentUrl);
                }
            });
        } catch (Exception e) {
            System.out.println("URL did not change within timeout");
            return false;
        }
    }
    
    /**
     * Checks if the page appears to be a modern SPA.
     */
    public boolean isModernSPA() {
        try {
            // Check for common SPA indicators
            Boolean hasSPAIndicators = (Boolean) jsExecutor.executeScript(
                "return typeof React !== 'undefined' || " +
                "typeof Vue !== 'undefined' || " +
                "typeof angular !== 'undefined' || " +
                "document.querySelector('[data-reactroot]') !== null || " +
                "document.querySelector('[ng-app]') !== null || " +
                "document.querySelector('[data-vue-app]') !== null || " +
                "window.location.hash.length > 1"
            );
            
            return hasSPAIndicators;
        } catch (Exception e) {
            return false;
        }
    }
}