package org.MakeMyTrip;

import java.time.Duration;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AutomationTask_MakeMyTrip extends MMTUtility {
    private static final Logger logger = LogManager.getLogger(AutomationTask_MakeMyTrip.class);

    @BeforeTest
    public void setUp() {
        logger.info("Starting MakeMyTrip Automation Script...");
        
        cleanupOldReports();

        options = new ChromeOptions();
        
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--incognito");

        driver = new ChromeDriver(options);
        ((ChromeDriver) driver).executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", 
            Collections.singletonMap("source", "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().deleteAllCookies();

        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        js = (JavascriptExecutor) driver;

        logger.info("Step 1: Opening MakeMyTrip website...");
        driver.get("https://www.makemytrip.com/");
    }

    @Test
    public void RunTest() {
        try {
            logger.info("Step 1: Logo validation...");
            checkLogo();
            
            logger.info("Step 2: Closing popup...");
            closePopUp();
            
            logger.info("Step 3: Clicking Hotel Menu...");
            clickMenuHotel();
            
            logger.info("Step 4: Minimizing chatbot...");
            minimizeMyraBot();
            
            logger.info("Step 5: Searching for city...");
            clickAndSearchCity(driver, "Mumbai");
            
            logger.info("Step 6: Selecting city from suggestions...");
            // Notice: Updated signature based on the static wait/js available in parent
            selectMainCity("Mumbai", "City in Maharashtra");
            
            logger.info("Step 7: Closing calendar...");
            closeCalendarUsingESC();
           
            logger.info("Step 8: Clicking Search button...");
            clickSearch();

            logger.info("Step 9: Fetching hotel counts...");
            GetTotalNumberofHotels(wait, "Mumbai");
            
            logger.info("Step 10: Extracting all hotel names...");
            GetAllHotelNames(driver, wait, js);

        } catch (Exception e) {
            captureScreenshot(driver, "MMT_Failure_During_Execution");
            logger.error("Test Failed! Error: " + e.getMessage());
            throw e; 
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
             driver.quit();
             logger.info("Browser closed. Automation Script Completed.");
        }
    }
}