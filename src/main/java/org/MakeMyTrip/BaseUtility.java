package org.MakeMyTrip;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseUtility {
    protected static final Logger logger = LogManager.getLogger(BaseUtility.class);
    public static WebDriver driver;
    public static WebDriverWait wait;
    public static JavascriptExecutor js;
    public static ChromeOptions options;

    // Standardized Screenshots
    public static void captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            File directory = new File("./Screenshots/");
            if (!directory.exists()) directory.mkdirs();
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String time = new SimpleDateFormat("HHmmss").format(new Date());
            com.google.common.io.Files.copy(source, new File(directory, screenshotName + "_" + time + ".png"));
        } catch (Exception e) {
            logger.error("Screenshot failed: " + e.getMessage());
        }
    }

    // Generic Scroll and Click helper
    public void scrollAndClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    // Cleanup helper
    public static void cleanupOldReports() {
        String[] directories = {"./Screenshots", "./test-output"};
        for (String path : directories) {
            File folder = new File(path);
            if (folder.exists() && folder.listFiles() != null) {
                for (File f : folder.listFiles()) f.delete();
                logger.info("Cleaned: " + path);
            }
        }
    }
    
    public void clickElement(WebDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        try {
            WebElement element = localWait.until(ExpectedConditions.elementToBeClickable(locator));
            // Scroll to element to ensure it is in view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            element.click();
            logger.info("Clicked on element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to click element: " + locator + " | " + e.getMessage());
            throw e;
        }
    }
}