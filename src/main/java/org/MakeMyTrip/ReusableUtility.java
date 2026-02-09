package org.MakeMyTrip;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

public class ReusableUtility 
{
	private static final Logger logger = LogManager.getLogger(ReusableUtility.class);
    public static WebDriver driver;
    public static WebDriverWait wait;
    public static JavascriptExecutor js;
    public static TakesScreenshot ts;
	
    public static void closePopUp() {
        try {
            By crossIcon = By.xpath("//span[@class='commonModal__close']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(crossIcon));
            WebElement ClickcrossIcon = driver.findElement(crossIcon);
            boolean isDisplayed = ClickcrossIcon.isDisplayed();

            if (isDisplayed) {
                ClickcrossIcon.click();
                logger.info("Pop up found and  closed");
                captureScreenshot(driver,"Pop up found and  closed");
            } else {
            	logger.info("Pop up not found");
            	captureScreenshot(driver,"Pop up not found");
            }
        } catch (TimeoutException e) {
        	captureScreenshot(driver,"Popup_Not_Found");
        	logger.info("Pop up not shown the screen!!!");
        }
    }
    
    public static void minimizeMyraBot() {
        try {
            By minimizeLocator = By.xpath("//img[@alt='minimize']/parent::div");
            
            // We check if the element exists in the DOM first
            if (driver.findElements(minimizeLocator).size() > 0) {
                WebElement minimizeBtn = wait.until(ExpectedConditions.elementToBeClickable(minimizeLocator));
                minimizeBtn.click();
                logger.info("Myra Bot found and minimized.");
                captureScreenshot(driver,"Myra Bot found and minimized.");
            } else {
                logger.info("Myra Bot not found on this page, skipping.");
                captureScreenshot(driver,"Myra Bot not found on this page, skipping.");
            }
        } catch (Exception e) {
        	captureScreenshot(driver,"Popup_Not_Found");
            logger.info("Myra Bot appeared but could not be clicked: " + e.getMessage());
        }
    }
    
    public static void clickMenuHotel()
    {
    	logger.info("Click Hotel Menu");
    	captureScreenshot(driver,"Click Hotel Menu");
    	String MenuHotelButtonLocator = "//li[@class=\"menu_Hotels\"]";
    	By MenuHotel = By.xpath(MenuHotelButtonLocator);
    	WebElement HotelMenuButton = wait.until(ExpectedConditions.elementToBeClickable(MenuHotel));
    	HotelMenuButton.click();
    }

  
    public static void clickAndSearchCity(WebDriver wd, String text) {
    	logger.info("Clicking Search box: ");
        String CityButtonLocator = "//input[@id=\"city\" and @type = \"text\"]";
        By CityDate = By.xpath(CityButtonLocator);
        WebElement DeparturDateButton = wait.until(ExpectedConditions.elementToBeClickable(CityDate));
        DeparturDateButton.click();
        
        String SearchBox = "//input[@title=\"Where do you want to stay?\"]";
        By searchLocator = By.xpath(SearchBox);
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchLocator));
        WebElement textBoxWebElement = wd.switchTo().activeElement();
        textBoxWebElement.sendKeys(text);
        logger.info("Searching : " + text);
    }

   
    
    public static void selectMainCity(WebDriver wd, WebDriverWait wait, JavascriptExecutor js, String expectedCityName, String expectedCityType)
    {

        By suggestionRows = By.xpath("//li[contains(@class,'react-autosuggest__suggestion')]");
        logger.info("Waiting for city autosuggestions...");
        
        List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(suggestionRows));
        logger.info("Total autosuggestions found: " + rows.size());

        for (WebElement row : rows) {
            try {
                WebElement cityNameEle = row.findElement(By.xpath(".//span[@class=\"blackText\"]")); // hightlight first name like city

                WebElement cityTypeEle = row.findElement(By.xpath(".//p[@class='sr_city']/div[2]")); // hightlight last name  city type


                String cityName = cityNameEle.getText().trim();
                String cityType = cityTypeEle.getText().trim();

                logger.info("Checking -> " + cityName + " | " + cityType);

                // Exact match on secondary text
                if (cityName.equalsIgnoreCase(expectedCityName) && cityType.equalsIgnoreCase(expectedCityType)) {

                    js.executeScript("arguments[0].scrollIntoView(true);", row);
                    js.executeScript("arguments[0].click();", row);
                    captureScreenshot(driver,"Selected main Mumbai city");
                    logger.info("Selected main Mumbai city");
                    return;
                }

            } catch (NoSuchElementException e) {
            	 captureScreenshot(driver,"Skipping invalid autosuggestion row");
                logger.warn("Skipping invalid autosuggestion row");
            }
        }

        throw new RuntimeException("Mumbai main city not found");
    }


   
    public static void clickSearch() {
        try {
            WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("hsw_search_button")));
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", searchBtn);
           
            long randomWait = 1500 + (long)(Math.random() * 1500);
            Thread.sleep(randomWait); 
           
            js.executeScript("arguments[0].click();", searchBtn);
            logger.info("Search button clicked with randomized delay.");
            
        } catch (Exception e) {
            captureScreenshot(driver, "Search_Failure");
            throw new RuntimeException("Final search attempt failed", e);
        }
    }
    
    
    public static void closeCalendarByClickingHeader() {
        try {
            By calendar = By.xpath("//div[contains(@class,'DayPicker')]");
            wait.until(ExpectedConditions.visibilityOfElementLocated(calendar));

            WebElement hotelsHeader = driver.findElement(By.xpath("//span[text()='Hotels']"));
            hotelsHeader.click();

            wait.until(ExpectedConditions.invisibilityOfElementLocated(calendar));
            logger.info("Calendar closed by clicking Hotels header");
            captureScreenshot(driver,"Calendar not visible");

        } catch (TimeoutException e) {
        	captureScreenshot(driver,"Calendar not visible");
        	logger.info("Calendar not visible");
        }
    }
    
    
    public static void closeCalendarUsingESC() {
        try {
            new Actions(driver)
                    .sendKeys(Keys.ESCAPE)
                    .perform();
            logger.info("Calendar closed using ESC");
            captureScreenshot(driver,"Calendar not open");
        } catch (Exception e) {
        	captureScreenshot(driver,"Calendar not open");
        	logger.info("Calendar not open");
        }
    }
    
    
    public static void GetTotalNumberofHotels(WebDriverWait wait, String city) {
        try {
            By AvailabulHotelNumber = By.xpath("//div[@id=\"seoH1DontRemoveContainer\"]//h1");

            WebElement getValue = wait.until(ExpectedConditions.visibilityOfElementLocated(AvailabulHotelNumber));
            String text = getValue.getText();
            logger.info("Subtitle Text: " + text);

            if (text.toLowerCase().contains(city)) {
            	captureScreenshot(driver,"Total Hotels Found:");
                logger.info("Total Hotels Found: " + text);
            } else {
            	captureScreenshot(driver,"Unexpected subtitle text");
                logger.warn("Unexpected subtitle text: " + text);
            }
        } catch (TimeoutException e) {
        	captureScreenshot(driver,"Timeout while waiting for subtitle text about Hotels");
            logger.error("Timeout while waiting for subtitle text about Hotels.", e);
        } catch (Exception e) {
        	captureScreenshot(driver,"Unexpected error occurred in GetTotalNumberofHotels()");
            logger.error("Unexpected error occurred in GetTotalNumberofHotels()", e);
        }
    }
    
    public static void GetAllHotelNames( WebDriver wd, WebDriverWait wait, JavascriptExecutor js) {

        By tupleWrapperLocator = By.xpath("//div[contains(@id,'Listing_hotel')]");
        By hotelNameLocator = By.xpath(".//span[@class='wordBreak appendRight10']");
        By endOfListLocator = By.xpath("//p[text()=\"That's all the options we've got\"]");
        logger.info("Scrolling to load all Hotels...");

        try {
            while (true) {
                List<WebElement> currentList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(tupleWrapperLocator));

                // End-of-list check
                if (!wd.findElements(endOfListLocator).isEmpty()) {
                	captureScreenshot(driver,"Reached end of hotel list");
                	logger.info("Reached end of hotel list");
                    break;
                }

                if (currentList.size() < 3) {
                	captureScreenshot(driver,"Not enough hotel cards to scroll further");
                    logger.warn("Not enough hotel cards to scroll further");
                    break;
                }

                js.executeScript("arguments[0].scrollIntoView({behavior:'smooth', block:'end'});", currentList.get(currentList.size() - 3) );
                Thread.sleep(1200); // allow lazy loading
            }

            List<WebElement> finalList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tupleWrapperLocator));

            logger.info("Total loaded hotel listings: " + finalList.size());
            System.out.println("--------- HOTEL LIST ----------");

            int count = 1;
            for (WebElement row : finalList) {
                try {
                    String hotelName = row.findElement(hotelNameLocator).getText().trim();

                    if (!hotelName.isEmpty()) {
                        logger.info("Hotel: " + hotelName);
                        System.out.println(count + ". " + hotelName);
                        count++;
                    }
                } catch (NoSuchElementException e) {
                    logger.warn("Hotel name not found in a listing, skipping...");
                }
            }

            System.out.println("Total number of hotels after filters: " + (count - 1));

        } catch (TimeoutException e) {
            logger.error("Timed out while loading hotel listings", e);
            captureScreenshot(driver,"Failure_During_Hotel list did not load in time");
            throw new RuntimeException("Hotel list did not load in time", e);

        } catch (InterruptedException e) {
            logger.error("Thread interrupted during scrolling", e);
            captureScreenshot(driver,"Failure_During_scrolling");
            Thread.currentThread().interrupt();

        } catch (Exception e) {
            logger.error("Unexpected error while fetching hotel names", e);
            captureScreenshot(driver,"Failure_During_scrolling");
            throw new RuntimeException("Failed to fetch hotel names", e);
        }
    }

    public static void captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            File directory = new File("./Screenshots/");
            if (!directory.exists()) {
                directory.mkdirs(); 
            }

            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            
            String time = new java.text.SimpleDateFormat("HHmmss").format(new java.util.Date());
            File destination = new File(directory, screenshotName + "_" + time + ".png");
            
            com.google.common.io.Files.copy(source, destination);
        } catch (Exception e) {
            logger.error("Exception while taking screenshot: " + e.getMessage());
        }
    }
   
    public static void cleanupOldReports() {
        String[] directories = {"./Screenshots", "./allure-results", "./test-output"};
        for (String path : directories) {
            File folder = new File(path);
            if (folder.exists()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File f : files) f.delete();
                }
                logger.info("Cleaned: " + path);
            }
        }
    }
}
