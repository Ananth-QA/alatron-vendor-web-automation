package tests.auth;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.auth.AuthPage;

import java.time.Duration;
import java.util.Properties;
import java.io.InputStream;

public class VendorAuthTest {

    WebDriver driver;
    AuthPage auth;
    Properties prop = new Properties();

    @BeforeMethod
    public void setup() throws Exception {
        InputStream input =
                getClass().getClassLoader().getResourceAsStream("config.properties");
        prop.load(input);

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(prop.getProperty("base.url"));

        auth = new AuthPage(driver);
    }

    @Test
    public void verifyCompleteVendorAuthFlow() throws Exception {

        // 1️⃣ Enter mobile → Proceed
        auth.enterMobileNumber();
        auth.clickProceed();

        // Wait until OTP screen loads using Edit Number link
        Assert.assertTrue(auth.waitForOtpScreen(),
                "OTP screen should appear after clicking proceed");

        // 2️⃣ Click Edit Number → verify number persists
        auth.clickEditNumber();

        String actualNumber = auth.getEnteredMobileNumberOnly();
        Assert.assertTrue(
                actualNumber.contains(prop.getProperty("mobile.number")),
                "Mobile field should contain the entered mobile number"
        );
        
//        auth.clearMobileField();
        
        
        String newNumber = auth.clearAndEnterDynamicMobile();
        auth.clickProceed();
        Assert.assertTrue(auth.getEnteredMobileNumberOnly().contains(newNumber));

     // Click Proceed with empty number
//     auth.clickProceed();

     // (You can add validation assertion later if needed)

     // 4️⃣ Enter number again
//     auth.enterMobileNumber();
        
        
        // 3️⃣ Click Proceed Again
//        auth.clickProceed();
        Assert.assertTrue(auth.waitForOtpScreen(),
                "OTP screen should appear again after clicking proceed");

        // 4️⃣ Wait 30 sec → Resend OTP
        Thread.sleep(30000);
        auth.waitAndClickResendOtp();
//        Assert.assertTrue(auth.isToastDisplayed(),
//                "Toast should appear after clicking resend OTP");

        // 5️⃣ Enter OTP and Verify
        auth.enterOtp();
        auth.clickVerify();

        // Registration / next flow validation will be added later
        
     // 6️⃣ After OTP Success → Registration Page
        Assert.assertTrue(auth.isRegistrationPageVisible(),
                "Registration page must be visible after OTP");

        // Fill Sections
        auth.fillBusinessDetails();
        auth.fillAddressDetails();
        auth.uploadKycDocument();

        // Submit
        auth.clickCreateAccount();
        Thread.sleep(5000);

        // Verify
        Assert.assertTrue(auth.verifyAccountCreated(),
                "Account should be successfully created");

        
    }
    
    
    
    
    
    

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
