package org.MakeMyTrip;

import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AutomationTask extends ReusableUtility {
	private static final Logger logger = LogManager.getLogger(AutomationTask.class);
    public static ChromeOptions options;
    
    
    @BeforeTest
    public void setUp() {
    	logger.info("Starting MakeMyTrip Automation Script...");
        options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");

        options.addArguments("--incognito");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().deleteAllCookies();

        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        js = (JavascriptExecutor) driver;

        logger.info("Step 1 : Opening MakeMyTrip website...");
        driver.get("https://www.makemytrip.com/");

        System.out.println("Browser launched and application opened");
    }

    @Test
       public void RunTest() {
    	
    	try {
   
        logger.info("Step 2 : Closeing popup...");
        closePopUp();
        
        logger.info("Step 3 : Click Hotel Menu...");
        clickMenuHotel();
        
        logger.info("Step 4 : close chatboat");
        minimizeMyraBot();
        
        logger.info("Step 5 : Search city...");
        clickAndSearchCity(driver, "Mumbai");
        
        logger.info("Step 6 : Select City...");
        selectMainCity(driver, wait, js, "Mumbai","City in Maharashtra");
        
        logger.info("Step 7 : Close calender...");
        closeCalendarUsingESC();
       
        logger.info("Step 8 : Click search...");
        clickSearch();

        logger.info("Step 9 : Fetching total number of available buses...");
        GetTotalNumberofHotels(wait,"Mumbai");
        
        logger.info("Step 10 : Getting names of all available Hotels...");
        GetAllHotelNames(driver, wait, js);
    	}
    	catch (Exception e) {
            captureScreenshot("Failure_During_Execution");
            logger.error("Test Failed! Screenshot captured.");
            throw e; 
        }

       }
    
    
    @AfterTest
    public void tearDown() {

        if (driver != null) {
        	 driver.quit();
             logger.info("Step 10 : Automation Script Completed Successfully!");
        }
    }

}
