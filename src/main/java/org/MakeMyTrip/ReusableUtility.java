package org.MakeMyTrip;

import java.io.File;
import java.time.Duration;
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
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

public class ReusableUtility {
	private static final Logger logger = LogManager.getLogger(ReusableUtility.class);
	public static WebDriver driver;
	public static WebDriverWait wait;
	public static JavascriptExecutor js;
	public static TakesScreenshot ts;
	public static ChromeOptions options;

	protected By Getstarted = By.xpath("//span[text()=\"Get started\"]");
	protected By NextButton = By.xpath("//span[text()=\"Next\" or text()=\"Next step\"]");
	protected By Continue = By.xpath("//span[text()=\"Continue\"]");
	protected By Self = By.xpath("//span[text()=\"Self\"]");
	protected By SelfMale = By.xpath("//span[text()='Self']/following::div[text()='Male'][1]");
	private By ageInput = By.xpath("//input[@placeholder='Your age']");
	private By pinCodeInput = By.xpath("//input[@placeholder='Enter your pin code']");
	private By calculatePremiun = By
			.xpath("(//span[contains(@class,\"mantine-Button-label\") and text()=\"Calculate Premium\"])[1]");
	private By sliderThumb = By.xpath("//div[@role='slider']");
	private By oneCrLabel = By.xpath("//span[text()='1 Cr' or text()='₹1 Cr']");
	private By calculateBtn = By.xpath("//button[.//span[text()='Calculate Premium']]");

	public static void closePopUp() {
		try {
			By crossIcon = By.xpath("//span[@class='commonModal__close']");
			wait.until(ExpectedConditions.visibilityOfElementLocated(crossIcon));
			WebElement ClickcrossIcon = driver.findElement(crossIcon);
			boolean isDisplayed = ClickcrossIcon.isDisplayed();

			if (isDisplayed) {
				ClickcrossIcon.click();
				logger.info("Pop up found and  closed");
				captureScreenshot(driver, "Pop up found and  closed");
			} else {
				logger.info("Pop up not found");
				captureScreenshot(driver, "Pop up not found");
			}
		} catch (TimeoutException e) {
			captureScreenshot(driver, "Popup_Not_Found");
			logger.info("Pop up not shown the screen!!!");
		}
	}

	public void checkLogo() {
		By logo = By.xpath("//img[@alt=\"Make My Trip\"]");
		boolean isDisplayed = driver.findElement(logo).isDisplayed();
		if (isDisplayed) {
			System.out.println("In MakeMyTrip Homepage");
		}
	}

	public static void minimizeMyraBot() {
		try {
			By minimizeLocator = By.xpath("//img[@alt='minimize']/parent::div");
			wait.until(ExpectedConditions.visibilityOfElementLocated(minimizeLocator));
			WebElement minimizeBtn = driver.findElement(minimizeLocator);
			boolean isDisplayed = minimizeBtn.isDisplayed();
			if (isDisplayed) {
				minimizeBtn.click();
				logger.info("Pop up found and  closed");
				captureScreenshot(driver, "Pop up found and  closed");
			} else {
				logger.info("Pop up not found");
				captureScreenshot(driver, "Pop up not found");
			}
		} catch (Exception e) {
			captureScreenshot(driver, "Popup_Not_Found");
			logger.info("Myra Bot appeared but could not be clicked: " + e.getMessage());
		}
	}

	public static void clickMenuHotel() {
		logger.info("Click Hotel Menu");
		captureScreenshot(driver, "Click Hotel Menu");
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

	public static void selectMainCity(WebDriver wd, WebDriverWait wait, JavascriptExecutor js, String expectedCityName,
			String expectedCityType) {

		By suggestionRows = By.xpath("//li[contains(@class,'react-autosuggest__suggestion')]");
		logger.info("Waiting for city autosuggestions...");

		List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(suggestionRows));
		logger.info("Total autosuggestions found: " + rows.size());

		for (WebElement row : rows) {
			try {
				WebElement cityNameEle = row.findElement(By.xpath(".//span[@class=\"blackText\"]")); // hightlight first
																										// name like
																										// city

				WebElement cityTypeEle = row.findElement(By.xpath(".//p[@class='sr_city']/div[2]")); // hightlight last
																										// name city
																										// type

				String cityName = cityNameEle.getText().trim();
				String cityType = cityTypeEle.getText().trim();

				logger.info("Checking -> " + cityName + " | " + cityType);

				// Exact match on secondary text
				if (cityName.equalsIgnoreCase(expectedCityName) && cityType.equalsIgnoreCase(expectedCityType)) {

					js.executeScript("arguments[0].scrollIntoView(true);", row);
					js.executeScript("arguments[0].click();", row);
					captureScreenshot(driver, "Selected main Mumbai city");
					logger.info("Selected main Mumbai city");
					return;
				}

			} catch (NoSuchElementException e) {
				captureScreenshot(driver, "Skipping invalid autosuggestion row");
				logger.warn("Skipping invalid autosuggestion row");
			}
		}

		throw new RuntimeException("Mumbai main city not found");
	}

	public static void clickSearch() {
		try {
			WebElement searchBtn = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath(".//button[@id=\"hsw_search_button\"]")));
			searchBtn.click();
			// js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block:
			// 'center'});", searchBtn);

//            long randomWait = 1500 + (long)(Math.random() * 1500);
//            Thread.sleep(randomWait); 
//           
//            js.executeScript("arguments[0].click();", searchBtn);
//            logger.info("Search button clicked with randomized delay.");

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
			captureScreenshot(driver, "Calendar not visible");

		} catch (TimeoutException e) {
			captureScreenshot(driver, "Calendar not visible");
			logger.info("Calendar not visible");
		}
	}

	public static void closeCalendarUsingESC() {
		try {
			new Actions(driver).sendKeys(Keys.ESCAPE).perform();
			logger.info("Calendar closed using ESC");
			captureScreenshot(driver, "Calendar not open");
		} catch (Exception e) {
			captureScreenshot(driver, "Calendar not open");
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
				captureScreenshot(driver, "Total Hotels Found:");
				logger.info("Total Hotels Found: " + text);
			} else {
				captureScreenshot(driver, "Unexpected subtitle text");
				logger.warn("Unexpected subtitle text: " + text);
			}
		} catch (TimeoutException e) {
			captureScreenshot(driver, "Timeout while waiting for subtitle text about Hotels");
			logger.error("Timeout while waiting for subtitle text about Hotels.", e);
		} catch (Exception e) {
			captureScreenshot(driver, "Unexpected error occurred in GetTotalNumberofHotels()");
			logger.error("Unexpected error occurred in GetTotalNumberofHotels()", e);
		}
	}

	public static void GetAllHotelNames(WebDriver wd, WebDriverWait wait, JavascriptExecutor js) {

		By tupleWrapperLocator = By.xpath("//div[contains(@id,'Listing_hotel')]");
		By hotelNameLocator = By.xpath(".//span[@class='wordBreak appendRight10']");
		By endOfListLocator = By.xpath("//p[text()=\"That's all the options we've got\"]");
		logger.info("Scrolling to load all Hotels...");

		try {
			while (true) {
				List<WebElement> currentList = wait
						.until(ExpectedConditions.presenceOfAllElementsLocatedBy(tupleWrapperLocator));

				// End-of-list check
				if (!wd.findElements(endOfListLocator).isEmpty()) {
					captureScreenshot(driver, "Reached end of hotel list");
					logger.info("Reached end of hotel list");
					break;
				}

				if (currentList.size() < 3) {
					captureScreenshot(driver, "Not enough hotel cards to scroll further");
					logger.warn("Not enough hotel cards to scroll further");
					break;
				}

				js.executeScript("arguments[0].scrollIntoView({behavior:'smooth', block:'end'});",
						currentList.get(currentList.size() - 3));
				Thread.sleep(1200); // allow lazy loading
			}

			List<WebElement> finalList = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tupleWrapperLocator));

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
			captureScreenshot(driver, "Failure_During_Hotel list did not load in time");
			throw new RuntimeException("Hotel list did not load in time", e);

		} catch (InterruptedException e) {
			logger.error("Thread interrupted during scrolling", e);
			captureScreenshot(driver, "Failure_During_scrolling");
			Thread.currentThread().interrupt();

		} catch (Exception e) {
			logger.error("Unexpected error while fetching hotel names", e);
			captureScreenshot(driver, "Failure_During_scrolling");
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
		String[] directories = { "./Screenshots", "./allure-results", "./test-output" };
		for (String path : directories) {
			File folder = new File(path);
			if (folder.exists()) {
				File[] files = folder.listFiles();
				if (files != null) {
					for (File f : files)
						f.delete();
				}
				logger.info("Cleaned: " + path);
			}
		}
	}

	public void selectInsuranceProduct(String brand, String product) {

		String xpath = String.format(
				"//div[contains(@class, 'mantine-Paper-root')][.//span[text()='%s'] and .//span[text()='%s']]", brand,
				product);
		WebElement productCard = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		productCard.click();
	}

	public String getHeaderText(String xpathExpression) {
		By policyHeader = By.xpath(xpathExpression);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(policyHeader)).getText();
	}

	public void selectHealthInsurance(String categoryName) {
		// Use String.format to inject the variable 'categoryName' into the XPath
		String healthXPath = String.format("//div[contains(@class, 'mantine-Flex-root')][.//span[text()='%s']]",
				categoryName);

		WebElement healthCard = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(healthXPath)));
		healthCard.click();
	}

	public void clickElement(WebDriver driver, By locator, int timeoutInSeconds) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
	    try {
	        // 1. Wait for presence first
	        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	        
	        // 2. Scroll to element to ensure it's in the viewport
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
	        
	        // 3. Wait for clickability and click
	        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
	    } catch (TimeoutException e) {
	        System.err.println("Failed to find element: " + locator + " within " + timeoutInSeconds + " seconds.");
	        throw e; // Rethrow to fail the TestNG test
	    }
	}

	public void fillPrimaryDetails(String age, String pincode) {
		WebElement ageElement = wait.until(ExpectedConditions.elementToBeClickable(ageInput));
		ageElement.clear();
		ageElement.sendKeys(age);

		WebElement pinElement = wait.until(ExpectedConditions.elementToBeClickable(pinCodeInput));
		pinElement.clear();
		pinElement.sendKeys(pincode);
	}

	public void setCoverAmount(String targetAmount) {
		// 1. Locate the specific toggle container
		WebElement box = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, '_coverChangeBox')]")));

		// 2. Identify display and plus button relative to the box
		WebElement display = box.findElement(By.xpath(".//span"));
		WebElement plusBtn = box.findElement(By.xpath(".//button[2]"));

		int safetyCounter = 0;
		while (safetyCounter < 15) {
			// Normalize text by replacing the non-breaking space with a standard space
			String currentText = display.getText().replace("\u00a0", " ").trim();

			// Log current value for debugging
			System.out.println("Currently at: " + currentText);

			// Check if target (e.g., "1 Cr") is reached
			if (currentText.contains(targetAmount)) {
				System.out.println("Target reached! Stopping clicks.");
				break;
			}

			// 3. Click and wait for the UI to catch up
			plusBtn.click();
			safetyCounter++;

			// IMPORTANT: Use a longer sleep (800ms) to prevent overshooting
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void moveSliderToOneCr(String text) {
		WebElement thumb = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@role='slider']")));

		// 10000000 represents 1 Cr in the aria-valuenow attribute
		while (!thumb.getAttribute("aria-valuenow").equals(text)) {
			thumb.sendKeys(Keys.ARROW_RIGHT);
			// Minimal sleep to allow the accessibility attribute to update
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}

	public void clickCalculate() {
		WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(calculateBtn));
		// Scrolls element into the center of the viewport
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
		wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
	}
	
		public void scrollAndClick(WebDriver driver, By locator, Duration timeout) {
	    WebDriverWait wait = new WebDriverWait(driver, timeout);
	    
	    // 1. Wait until the element is present in the DOM
	    WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

	    // 2. Check if the element is currently displayed in the viewport
	    if (!element.isDisplayed()) {
	        System.out.println("Element not visible, scrolling into view...");
	        // Scroll the element into the center of the viewport
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
	    }

	    // 3. Final wait for clickability to ensure any animations have finished
	    wait.until(ExpectedConditions.elementToBeClickable(element)).click();
	}

	// "//h1[contains(text(), 'Understand your policy')]"

}
