package com.selenium.notion.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * Helper class for debugging page structure and elements.
 */
public class PageDebugHelper {
    
    /**
     * Prints detailed information about the current page structure.
     */
    public static void debugPageStructure(WebDriver driver) {
        System.out.println("\n=== PAGE STRUCTURE DEBUG ===");
        
        try {
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Page Title: " + driver.getTitle());
            
            // Check for common input types
            debugInputFields(driver);
            
            // Check for buttons
            debugButtons(driver);
            
            // Check for forms
            debugForms(driver);
            
            // Check page source for keywords
            debugPageContent(driver);
            
            // Check for React/SPA indicators
            debugSPAIndicators(driver);
            
        } catch (Exception e) {
            System.out.println("Error during page debug: " + e.getMessage());
        }
        
        System.out.println("=== END PAGE DEBUG ===\n");
    }
    
    private static void debugInputFields(WebDriver driver) {
        System.out.println("\n--- INPUT FIELDS ---");
        
        try {
            List<WebElement> allInputs = driver.findElements(By.tagName("input"));
            System.out.println("Total input elements found: " + allInputs.size());
            
            for (int i = 0; i < allInputs.size() && i < 10; i++) {
                WebElement input = allInputs.get(i);
                System.out.println("Input " + (i+1) + ":");
                System.out.println("  Type: " + input.getAttribute("type"));
                System.out.println("  Name: " + input.getAttribute("name"));
                System.out.println("  ID: " + input.getAttribute("id"));
                System.out.println("  Placeholder: " + input.getAttribute("placeholder"));
                System.out.println("  Class: " + input.getAttribute("class"));
                System.out.println("  Visible: " + input.isDisplayed());
            }
        } catch (Exception e) {
            System.out.println("No input fields found or error: " + e.getMessage());
        }
    }
    
    private static void debugButtons(WebDriver driver) {
        System.out.println("\n--- BUTTONS ---");
        
        try {
            List<WebElement> buttons = driver.findElements(By.tagName("button"));
            System.out.println("Total button elements found: " + buttons.size());
            
            for (int i = 0; i < buttons.size() && i < 10; i++) {
                WebElement button = buttons.get(i);
                System.out.println("Button " + (i+1) + ":");
                System.out.println("  Text: " + button.getText());
                System.out.println("  Type: " + button.getAttribute("type"));
                System.out.println("  Class: " + button.getAttribute("class"));
                System.out.println("  Visible: " + button.isDisplayed());
            }
        } catch (Exception e) {
            System.out.println("No buttons found or error: " + e.getMessage());
        }
    }
    
    private static void debugForms(WebDriver driver) {
        System.out.println("\n--- FORMS ---");
        
        try {
            List<WebElement> forms = driver.findElements(By.tagName("form"));
            System.out.println("Total form elements found: " + forms.size());
            
            for (int i = 0; i < forms.size(); i++) {
                WebElement form = forms.get(i);
                System.out.println("Form " + (i+1) + ":");
                System.out.println("  Action: " + form.getAttribute("action"));
                System.out.println("  Method: " + form.getAttribute("method"));
                System.out.println("  Class: " + form.getAttribute("class"));
            }
        } catch (Exception e) {
            System.out.println("No forms found or error: " + e.getMessage());
        }
    }
    
    private static void debugPageContent(WebDriver driver) {
        System.out.println("\n--- PAGE CONTENT ANALYSIS ---");
        
        try {
            String pageSource = driver.getPageSource().toLowerCase();
            
            String[] keywords = {"email", "password", "login", "sign in", "continue", "submit", "verification"};
            for (String keyword : keywords) {
                boolean found = pageSource.contains(keyword);
                System.out.println("Contains '" + keyword + "': " + found);
            }
            
            // Check for React/Vue/Angular indicators
            String[] frameworks = {"react", "vue", "angular", "ng-", "data-reactroot"};
            for (String framework : frameworks) {
                boolean found = pageSource.contains(framework);
                System.out.println("Contains '" + framework + "': " + found);
            }
            
        } catch (Exception e) {
            System.out.println("Error analyzing page content: " + e.getMessage());
        }
    }
    
    private static void debugSPAIndicators(WebDriver driver) {
        System.out.println("\n--- SPA/DYNAMIC CONTENT INDICATORS ---");
        
        try {
            // Check for loading indicators
            List<WebElement> loadingElements = driver.findElements(By.xpath("//*[contains(@class, 'loading') or contains(@class, 'spinner') or contains(text(), 'Loading')]"));
            System.out.println("Loading indicators found: " + loadingElements.size());
            
            // Check for div elements (common in SPAs)
            List<WebElement> divs = driver.findElements(By.tagName("div"));
            System.out.println("Total div elements: " + divs.size());
            
            // Check for script tags
            List<WebElement> scripts = driver.findElements(By.tagName("script"));
            System.out.println("Script tags found: " + scripts.size());
            
            // Check if body has specific classes
            WebElement body = driver.findElement(By.tagName("body"));
            System.out.println("Body class: " + body.getAttribute("class"));
            
        } catch (Exception e) {
            System.out.println("Error checking SPA indicators: " + e.getMessage());
        }
    }
    
    /**
     * Saves page source to help with debugging.
     */
    public static void savePageSource(WebDriver driver, String filename) {
        try {
            String pageSource = driver.getPageSource();
            System.out.println("Page source length: " + pageSource.length() + " characters");
            System.out.println("First 500 characters:");
            System.out.println(pageSource.substring(0, Math.min(500, pageSource.length())));
        } catch (Exception e) {
            System.out.println("Error getting page source: " + e.getMessage());
        }
    }
}