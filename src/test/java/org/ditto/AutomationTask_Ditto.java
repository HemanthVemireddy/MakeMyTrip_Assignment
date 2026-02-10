package org.ditto;

import org.testng.annotations.BeforeTest;
import java.time.Duration;
import java.util.Collections;

import org.MakeMyTrip.AutomationTask_MakeMyTrip;
import org.MakeMyTrip.ReusableUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AutomationTask_Ditto extends ReusableUtility {
	private static final Logger logger = LogManager.getLogger(AutomationTask_Ditto.class);

	@BeforeTest
	public void setUp() {
		logger.info("Starting Ditto Automation Script...");
		options = new ChromeOptions();

		// 1. Standard Stealth Arguments
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		options.setExperimentalOption("useAutomationExtension", false);
		options.addArguments("--disable-blink-features=AutomationControlled");
		options.addArguments("--disable-notifications");
		options.addArguments("--start-maximized");
		options.addArguments("--incognito");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");

		driver = new ChromeDriver(options);

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
		driver.manage().deleteAllCookies();

		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		js = (JavascriptExecutor) driver;

		logger.info("Step 1: Opening MakeMyTrip website...");
		driver.get("https://app.joinditto.in/");

		System.out.println("Browser launched with Stealth mode and application opened");

	}

	@Test
	public void RunTest() {

		try {
			String headerXpath = "//span[contains(text(),\"Insurance made simple\")]";
			String expectedText = "Ditto - Insurance made simple";
			String actualHeader = getHeaderText(headerXpath);
			Assert.assertEquals(actualHeader, expectedText, "The policy details page did not load correctly.");
			
			selectHealthInsurance("Health insurance");
	
			clickElement(driver,Getstarted, 15);
			
			selectInsuranceProduct("HDFCERGO","Optima Secure");
			
			clickElement(driver,NextButton, 15);
			clickElement(driver,NextButton, 15);
			clickElement(driver,NextButton, 15);
			clickElement(driver,Continue, 15);
			clickElement(driver,Self, 15);
			clickElement(driver,SelfMale, 15);
			clickElement(driver,NextButton, 15);
			fillPrimaryDetails("27","500032");
			//setCoverAmount("1 Cr");
			moveSliderToOneCr("10000000");
			clickCalculate();

		} catch (Exception e) {
			captureScreenshot(driver, "Failure_During_Execution");
			logger.error("Test Failed! Screenshot captured.");
			throw e;
		}
	}

//	@AfterTest
//	public void tearDown() {
//
//		if (driver != null) {
//			driver.quit();
//			logger.info("Step 10 : Automation Script Completed Successfully!");
//		}
//	}

}
