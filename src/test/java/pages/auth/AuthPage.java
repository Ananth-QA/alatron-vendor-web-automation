package pages.auth;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import dev.failsafe.internal.util.Assert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;

public class AuthPage {

    private static final Logger log = LogManager.getLogger(AuthPage.class);

    private WebDriver driver;
    private WebDriverWait wait;
    private Properties prop = new Properties();

    public AuthPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            InputStream input =
                    getClass().getClassLoader().getResourceAsStream("config.properties");
            prop.load(input);
            log.info("config.properties loaded successfully");
        } catch (Exception e) {
            log.error("Failed to load config.properties", e);
            throw new RuntimeException("Failed to load config.properties");
        }
    }

    private By getBy(String key) {
        log.debug("Fetching locator for key: {}", key);
        return By.xpath(prop.getProperty(key));
    }

    /* ========= LOGIN ========= */

    public void enterMobileNumber() {
        String mobile = prop.getProperty("mobile.number");
        log.info("Entering mobile number: {}", mobile);

        WebElement input =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        getBy("phone.input")));
        input.clear();
        input.sendKeys(mobile);

        log.info("Mobile number entered successfully");
    }

    public void clickProceed() {
        log.info("Clicking Proceed button");
        wait.until(ExpectedConditions.elementToBeClickable(
                getBy("proceed.button"))).click();
        log.info("Proceed button clicked");
    }

    /* ========= OTP PAGE HANDLING ========= */

    public boolean waitForOtpScreen() {
        log.info("Waiting for OTP screen (Edit Number link visible)");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    getBy("edit.number.link")));
            log.info("OTP screen loaded successfully");
            return true;
        } catch (Exception e) {
            log.error("OTP screen did not load", e);
            return false;
        }
    }

    public void clickEditNumber() {
        log.info("Clicking Edit Number link");
        wait.until(ExpectedConditions.elementToBeClickable(
                getBy("edit.number.link"))).click();
        log.info("Edit Number clicked, returned to login page");
    }

//    public String getEnteredMobileNumber() {
//        WebElement input =
//                wait.until(ExpectedConditions.visibilityOfElementLocated(
//                        getBy("phone.input")));
//
//        String value = input.getAttribute("value");
//        log.info("Fetched mobile number from input: {}", value);
//        return value;
//    }

    public String getEnteredMobileNumberOnly() {
        WebElement input =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        getBy("phone.input")));

        String value = input.getAttribute("value");

        log.info("Raw number in input: {}", value);

        // Remove spaces + plus + countrycode automatically
        value = value.replaceAll("[^0-9]", ""); 

        log.info("Extracted pure numeric mobile number: {}", value);
        return value;
    }
//    public void clearMobileField() {
//        log.info("Clearing mobile number field");
//        WebElement input =
//                wait.until(ExpectedConditions.visibilityOfElementLocated(
//                        getBy("phone.input")));
//
//        input.clear();
//        log.info("Mobile number field cleared");
//        
//        input.sendKeys("2365897456");
//    }

    
    public String clearAndEnterDynamicMobile() {

        log.info("Clearing mobile number using keyboard backspace...");

        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(getBy("phone.input"))
        );

        input.click();   // focus inside field

        String existingValue = input.getAttribute("value");
        int length = existingValue.length();

        // Delete each character using BACKSPACE
        for (int i = 0; i < length; i++) {
            input.sendKeys(Keys.BACK_SPACE);
        }

        log.info("Existing mobile cleared successfully");

        // Generate dynamic number
        String newMobile = generateRandomMobile();
        log.info("Typing new dynamic mobile number: {}", newMobile);

        input.sendKeys(newMobile);

        log.info("Mobile number updated successfully");

        return newMobile;   // useful for validation later
        
        
        
    }    
    
    private String generateRandomMobile() {
	// TODO Auto-generated method stub
    	String prefix = "9"; // keep realistic start digit
        long random = (long)(Math.random() * 1000000000L);
        return prefix + String.format("%09d", random);
}
    
    

	public void waitAndClickResendOtp() {
        log.info("Waiting for Resend OTP link to become clickable");
        wait.until(ExpectedConditions.elementToBeClickable(
                getBy("resend.otp.link"))).click();
        log.info("Resend OTP clicked");
    }

//    public boolean isToastDisplayed() {
//        log.info("Checking toast message visibility");
//        boolean displayed = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                getBy("toast.message"))).isDisplayed();
//        log.info("Toast message displayed: {}", displayed);
//        return displayed;
//    }
	
	

    public void enterOtp() {
        String otp = prop.getProperty("otp.value");
        log.info("Entering OTP: {}", otp);

        List<WebElement> inputs =
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        getBy("otp.inputs")));

        for (int i = 0; i < inputs.size(); i++) {
            inputs.get(i).sendKeys(String.valueOf(otp.charAt(i)));
            log.debug("Entered OTP digit {}", (i + 1));
        }

        log.info("OTP entered successfully");
    }

    public void clickVerify() {
        log.info("Clicking Verify button");
        wait.until(ExpectedConditions.elementToBeClickable(
                getBy("verify.button"))).click();
        log.info("Verify button clicked");
    }
    
    
    /* ========= REGISTRATION PAGE ========= */

    public boolean isRegistrationPageVisible() {
        log.info("Checking registration page visibility");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                getBy("registration.header"))).isDisplayed();
    }

    /* -------- BUSINESS DETAILS -------- */

    public void fillBusinessDetails() {
        log.info("Filling Business Details Section");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                getBy("business.name")))
                .sendKeys(prop.getProperty("business.name.value"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                getBy("contact.person")))
                .sendKeys(prop.getProperty("contact.person.value"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                getBy("email.input")))
                .sendKeys(prop.getProperty("email.value"));

        WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(
                getBy("phone.field")));

        String disabled = phone.getAttribute("disabled");
        Assert.notNull(disabled, "Phone number field must be disabled");
        log.info("Phone number is disabled & validated");
    }

    /* -------- ADDRESS SECTION -------- */
    public void clickDropdownAndSelect(By dropdownLocator, By optionLocator) {

        log.info("Clicking dropdown element");
        WebElement dropdown =
                wait.until(ExpectedConditions.elementToBeClickable(dropdownLocator));
        dropdown.click();

        log.info("Selecting option from dropdown");
        WebElement option =
                wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
        option.click();

        log.info("Dropdown selection completed successfully");
    }
    public void fillAddressDetails() {

        log.info("Filling Address Details Section");

        // ===== Province =====
        clickDropdownAndSelect(
                getBy("province.dropdown"),
                getBy("province.first.option")
        );
        log.info("Province Selected");

        // ===== City =====
        clickDropdownAndSelect(
                getBy("city.dropdown"),
                getBy("city.first.option")
        );
        log.info("City Selected");

        // ===== District =====
        clickDropdownAndSelect(
                getBy("district.dropdown"),
                getBy("district.first.option")
        );
        log.info("District Selected");

        // ===== Village =====
        clickDropdownAndSelect(
                getBy("village.dropdown"),
                getBy("village.first.option")
        );
        log.info("Village Selected");

        // ===== Street Input =====
        log.info("Entering Street Details");
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                getBy("street.input")))
                .sendKeys(prop.getProperty("street.value"));

        // ===== PINCODE =====
        log.info("Entering Pincode");
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                getBy("pincode.input")))
                .sendKeys(prop.getProperty("pincode.value"));

        log.info("Address section completed successfully");
    }

    /* -------- FILE UPLOAD -------- */

    public void uploadKycDocument() {
        log.info("Uploading KYC Document...");

        WebElement upload =
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        getBy("upload.button")));

        upload.sendKeys(prop.getProperty("upload.file.path"));
        log.info("KYC File Uploaded Successfully");
    }

    /* -------- CREATE ACCOUNT -------- */

    public void clickCreateAccount() {
        log.info("Clicking Create Account Button");

        wait.until(ExpectedConditions.elementToBeClickable(
                getBy("create.account.button"))).click();

        log.info("Create Account button clicked");
    }

    public boolean verifyAccountCreated() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                    getBy("success.message"))).isDisplayed();
        } catch (Exception e) {
            log.warn("Success message not found");
            return false;
        }
    }

    
    
    

	
}
