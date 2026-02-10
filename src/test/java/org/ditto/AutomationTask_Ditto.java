package org.ditto;

import org.MakeMyTrip.DittoUtility; // Use DittoUtility instead of MMTUtility
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Collections;

/**
 * Updated Runner Class for Ditto Automation.
 * Inherits from DittoUtility to access insurance-specific methods and locators.
 */
public class AutomationTask_Ditto extends DittoUtility {
    private static final Logger logger = LogManager.getLogger(AutomationTask_Ditto.class);

    @BeforeTest
    public void setUp() {
        logger.info("Initializing Browser for Ditto Automation...");
        options = new ChromeOptions();

        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--incognito");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        js = (JavascriptExecutor) driver;

        logger.info("Navigating to Ditto App...");
        driver.get("https://app.joinditto.in/");
    }

    @Test
    public void runDittoInsuranceTest() {
        try {
            // 1. Verify Page Load
            String headerXpath = "//span[contains(text(),'Insurance made simple')]";
            String expectedText = "Insurance made simple"; 
            String actualHeader = getHeaderText(headerXpath);
            Assert.assertTrue(actualHeader.contains(expectedText), "Header text mismatch!");

            // 2. Select Category
            selectHealthInsurance("Health insurance");
            
            // 3. Navigate through the flow using static constants from DittoUtility
            clickElement(driver, LNK_GET_STARTED, 15);
            
            // 4. Select Product
            selectInsuranceProduct("HDFCERGO", "Optima Secure");
            
            // 5. Questionnaire Flow
            clickElement(driver, BTN_NEXT, 15); // How many people?
            clickElement(driver, BTN_NEXT, 15); // Existing diseases?
            clickElement(driver, BTN_NEXT, 15); // Habits?
            clickElement(driver, BTN_CONTINUE, 15); 
            
            // 6. Member Selection
            clickElement(driver, CHK_SELF, 15);
            clickElement(driver, RAD_SELF_MALE, 15);
            clickElement(driver, BTN_NEXT, 15);
            
            // 7. Personal Details
            fillPrimaryDetails("27", "500032"); 
            
            // 8. Adjust Coverage and Calculate
            //moveSliderToOneCr("10000000");
            
            // 9. calculate total premium 
            clickCalculate();
            
            // 10. validate
            validatePlanDetails();
            validateRiderToggle();

            logger.info("Ditto Insurance Flow completed successfully.");

        } catch (Exception e) {
            captureScreenshot(driver, "Ditto_Test_Failure");
            logger.error("Test Failed: " + e.getMessage());
            throw e;
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Browser session closed.");
        }
    }
}