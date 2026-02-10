package org.MakeMyTrip;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class DittoUtility extends BaseUtility {

    // --- Ditto Specific Locators ---
    public static final By LNK_GET_STARTED = By.xpath("//span[text()='Get started']");
    public static final By BTN_NEXT = By.xpath("//span[text()='Next' or text()='Next step']");
    public static final By BTN_CONTINUE = By.xpath("//span[text()='Continue']");
    public static final By CHK_SELF = By.xpath("//span[text()='Self']");
    public static final By RAD_SELF_MALE = By.xpath("//span[text()='Self']/following::div[text()='Male'][1]");
    public static final By INPUT_AGE = By.xpath("//input[@placeholder='Your age']");
    public static final By INPUT_PINCODE = By.xpath("//input[@placeholder='Enter your pin code']");
    public static final By BTN_CALCULATE = By.xpath("//button[.//span[contains(text(),'Calculate Premium')]]");

    // --- Ditto Specific Business Methods ---
    
    public String getHeaderText(String xpathExpression) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathExpression))).getText();
    }

    public void selectHealthInsurance(String categoryName) {
        String xpath = String.format("//div[contains(@class, 'mantine-Flex-root')][.//span[text()='%s']]", categoryName);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
    }

    public void selectInsuranceProduct(String brand, String product) {
        String xpath = String.format("//div[contains(@class, 'mantine-Paper-root')][.//span[text()='%s'] and .//span[text()='%s']]", brand, product);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
    }

    public void fillPrimaryDetails(String age, String pincode) {
        wait.until(ExpectedConditions.elementToBeClickable(INPUT_AGE)).sendKeys(age);
        wait.until(ExpectedConditions.elementToBeClickable(INPUT_PINCODE)).sendKeys(pincode);
    }

    public void moveSliderToOneCr(String targetValue) {
        WebElement thumb = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@role='slider']")));
        while (!thumb.getAttribute("aria-valuenow").equals(targetValue)) {
            thumb.sendKeys(Keys.ARROW_RIGHT);
        }
    }

    public void clickCalculate() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(BTN_CALCULATE));
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
        btn.click();
    }
    
    
    public void validatePlanDetails() {
        logger.info("Starting Plan Details Validation...");

        String memberXpath = "//span[text()='Members to be included']//following::span[text()='Self']";
        String memberInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(memberXpath))).getText();
        
        Assert.assertTrue(memberInfo.contains("Self"), "Member info mismatch! Found: " + memberInfo);

        double basePremium = getPriceValue("//span[text()='Base Premium']/following-sibling::span");
        double ridersTotal = getPriceValue("//span[contains(text(), 'Recommended Add-ons')]/following::span");
        double displayedTotal = getPriceValue("//span[text()='Total Premium']/following::span[1]");

        double subTotal = basePremium + ridersTotal;
        double expectedTotalWithGST = subTotal * 1.18; // 18% GST


        Assert.assertEquals(displayedTotal, expectedTotalWithGST, 2.0, "Premium summation mismatch!");
        logger.info("Premium summation validated successfully.");

        double premium1Year = getPriceValue("(//span[contains(normalize-space(), '1 Year')]//following::span[2])[1]");
        double premium2Years = getPriceValue("(//span[contains(normalize-space(), '2 Year')]//following::div/span[3])[1]");

        Assert.assertTrue(premium2Years < (premium1Year * 2), "Multi-year discount was not applied!");
        logger.info("Discount validation passed.");
    }

    public void validateRiderToggle() {
        logger.info("Validating Rider Toggle functionality...");
        
        double initialTotal = getPriceValue("//span[text()='Total Premium']/following::span[1]");

        By riderLoc = By.xpath("(//span[text()='Optima Well-being']//preceding::input[@name='Optima Well-being'])[last()]");
        WebElement riderCheckbox = driver.findElement(riderLoc);
        
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", riderCheckbox);
        js.executeScript("arguments[0].click();", riderCheckbox);

        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

        double newTotal = getPriceValue("//span[text()='Total Premium']/following::span[1]");
        
        Assert.assertNotEquals(initialTotal, newTotal, "Total Premium did not update after toggling the rider!");
        logger.info("Rider toggle validated. Price changed from " + initialTotal + " to " + newTotal);
    }

    private double getPriceValue(String xpath) {
        try {
            String text = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).getText();
            return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            logger.error("Failed to extract price from XPath: " + xpath);
            return 0.0;
        }
    }
}