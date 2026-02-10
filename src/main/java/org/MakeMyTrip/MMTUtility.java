package org.MakeMyTrip;

import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * MMTUtility handles all MakeMyTrip specific actions. It inherits generic
 * browser actions from BaseUtility.
 */
public class MMTUtility extends BaseUtility {

	// --- Static Locators ---
	public static final By IMG_LOGO = By.xpath("//img[@alt='Make My Trip']");
	public static final By MODAL_CLOSE = By.xpath("//span[@class='commonModal__close']");
	public static final By MENU_HOTELS = By.xpath("//li[@class='menu_Hotels']");
	public static final By BTN_MYRA_MINIMIZE = By.xpath("//img[@alt='minimize']/parent::div");
	public static final By INPUT_CITY_TRIGGER = By.xpath("//input[@id='city' and @type='text']");
	public static final By INPUT_CITY_SEARCH_TEXT = By.xpath("//input[@title='Where do you want to stay?']");
	public static final By BTN_SEARCH = By.xpath("//button[@id='hsw_search_button']");
	public static final By LBL_HOTEL_COUNT = By.xpath("//div[@id='seoH1DontRemoveContainer']//h1");
	public static final By LISTING_CARDS = By.xpath("//div[contains(@id,'Listing_hotel')]");
	public static final By HOTEL_NAME_TEXT = By.xpath(".//span[@class='wordBreak appendRight10']");
	public static final By END_OF_LIST_MSG = By.xpath("//p[text()=\"That's all the options we've got\"]");

	// --- MakeMyTrip Business Methods ---

	public void checkLogo() {
		try {
			boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(IMG_LOGO)).isDisplayed();
			if (isDisplayed) {
				logger.info("Successfully validated MakeMyTrip Logo on Homepage.");
				System.out.println("In MakeMyTrip Homepage");
			}
		} catch (Exception e) {
			logger.error("Logo not found! Possibly not on the correct page.");
		}
	}

	public static void closePopUp() {
		try {
			WebElement closeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL_CLOSE));
			if (closeBtn.isDisplayed()) {
				closeBtn.click();
				logger.info("Pop-up closed successfully.");
			}
		} catch (TimeoutException e) {
			logger.info("Main pop-up did not appear.");
		}
	}

	public static void clickMenuHotel() {
		logger.info("Navigating to Hotels section.");
		wait.until(ExpectedConditions.elementToBeClickable(MENU_HOTELS)).click();
		captureScreenshot(driver, "Hotel_Menu_Clicked");
	}

	public static void minimizeMyraBot() {
		try {
			WebElement minimizeBtn = wait.until(ExpectedConditions.elementToBeClickable(BTN_MYRA_MINIMIZE));
			minimizeBtn.click();
			logger.info("Myra Chatbot minimized.");
		} catch (Exception e) {
			logger.info("Myra Chatbot not present.");
		}
	}

	public static void clickAndSearchCity(WebDriver wd, String text) {
		wait.until(ExpectedConditions.elementToBeClickable(INPUT_CITY_TRIGGER)).click();
		WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_CITY_SEARCH_TEXT));
		input.sendKeys(text);
		logger.info("Searching for city: " + text);
	}

	public static void selectMainCity(WebDriver wd, WebDriverWait wait, JavascriptExecutor js, String expectedCityName,
			String expectedCityType) {
		By suggestionRows = By.xpath("//li[contains(@class,'react-autosuggest__suggestion')]");
		List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(suggestionRows));

		for (WebElement row : rows) {
			try {
				String cityName = row.findElement(By.xpath(".//span[@class='blackText']")).getText().trim();
				String cityType = row.findElement(By.xpath(".//p[@class='sr_city']/div[2]")).getText().trim();

				if (cityName.equalsIgnoreCase(expectedCityName) && cityType.equalsIgnoreCase(expectedCityType)) {
					js.executeScript("arguments[0].scrollIntoView(true);", row);
					js.executeScript("arguments[0].click();", row);
					logger.info("City selected: " + cityName + " (" + cityType + ")");
					return;
				}
			} catch (NoSuchElementException e) {
				logger.warn("Skipping empty suggestion row.");
			}
		}
		throw new RuntimeException("Target city suggestion not found.");
	}

	public static void closeCalendarUsingESC() {
		try {
			new Actions(driver).sendKeys(Keys.ESCAPE).perform();
			logger.info("Calendar closed using ESC key.");
		} catch (Exception e) {
			logger.warn("Could not close calendar using ESC.");
		}
	}

	public static void clickSearch() {
		try {
			WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(BTN_SEARCH));
			searchBtn.click();
			logger.info("Search button clicked.");
		} catch (Exception e) {
			captureScreenshot(driver, "Search_Click_Failed");
			throw new RuntimeException("Failed to click Search button.", e);
		}
	}

	public static void GetTotalNumberofHotels(WebDriverWait wait, String city) {
		try {
			WebElement getValue = wait.until(ExpectedConditions.visibilityOfElementLocated(LBL_HOTEL_COUNT));
			String text = getValue.getText();
			logger.info("Hotel Count Results: " + text);
			System.out.println("Total Hotels in " + city + ": " + text);
		} catch (Exception e) {
			logger.error("Hotel count label not found.");
		}
	}

	public static void GetAllHotelNames(WebDriver wd, WebDriverWait wait, JavascriptExecutor js) {
		logger.info("Starting hotel name extraction...");
		try {
			// Lazy load/Scroll logic
			for (int i = 0; i < 3; i++) { // Scroll a few times to load results
				js.executeScript("window.scrollBy(0,1000)");
				Thread.sleep(1000);
			}

			List<WebElement> hotelNames = wd.findElements(HOTEL_NAME_TEXT);
			System.out.println("--------- HOTEL LIST ----------");
			int count = 1;
			for (WebElement name : hotelNames) {
				String hotelName = name.getText().trim();
				if (!hotelName.isEmpty()) {
					System.out.println(count++ + ". " + hotelName);
				}
				if (count > 20)
					break; // Limit output for console readability
			}
		} catch (Exception e) {
			logger.error("Error fetching hotel names: " + e.getMessage());
		}
	}

	public static void selectMainCity(String expectedCityName, String expectedCityType) {
		By suggestionRows = By.xpath("//li[contains(@class,'react-autosuggest__suggestion')]");

		List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(suggestionRows));

		for (WebElement row : rows) {
			try {
				String cityName = row.findElement(By.xpath(".//span[@class='blackText']")).getText().trim();
				String cityType = row.findElement(By.xpath(".//p[@class='sr_city']/div[2]")).getText().trim();

				if (cityName.equalsIgnoreCase(expectedCityName) && cityType.equalsIgnoreCase(expectedCityType)) {
					js.executeScript("arguments[0].scrollIntoView(true);", row);
					js.executeScript("arguments[0].click();", row);
					logger.info("City selected: " + cityName + " (" + cityType + ")");
					return;
				}
			} catch (NoSuchElementException e) {
				logger.warn("Skipping empty suggestion row.");
			}
		}
		throw new RuntimeException("Target city suggestion not found for: " + expectedCityName);
	}
}