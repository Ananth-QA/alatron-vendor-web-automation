package pages.dashboard;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import config.ConfigLoader;

import java.time.Duration;

public class PrivacySecurityPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public PrivacySecurityPage(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    private By get(String key){
        return By.xpath(ConfigLoader.get(key));
    }

    private void safeClick(String label, By locator){
        try{
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
            el.click();
            System.out.println("🟢 Clicked → " + label);
        }catch(Exception e){
            System.out.println("⚠️ Unable to click → " + label);
        }
    }

    private void safeVerify(String label, By locator){
        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            System.out.println("🟢 Verified → " + label);
        }catch(Exception e){
            System.out.println("⚠️ Missing → " + label);
        }
    }

    private void toggleSwitch(String label, By locator){
        try{
            WebElement toggle = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            String stateBefore = toggle.getAttribute("aria-checked");

            toggle.click();
            Thread.sleep(600);

            String stateAfter = toggle.getAttribute("aria-checked");

            System.out.println("🔁 " + label + " | Before: "
                    + stateBefore + " → After: " + stateAfter);

            // Toggle back for visibility test
            toggle.click();
            Thread.sleep(600);

        }catch(Exception e){
            System.out.println("⚠️ Toggle missing → " + label);
        }
    }
    
    
    
    public void clickConfirmation() {
        wait.until(ExpectedConditions
                .elementToBeClickable(get("confirmation.continue.button")))
                .click();

        System.out.println("✅ Confirmation popup Continue clicked successfully");
    }


    public void openProfile(){
        wait.until(ExpectedConditions.elementToBeClickable(get("profile.menu"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(get("profile.vendor.header")));
        System.out.println("🟢 Navigated to Profile Page Successfully");
    }

    
    

    // ================= OPEN PAGE =================
    public void openPrivacySecurity(){

        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("privacy.menu"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("privacy.page.header")));

            System.out.println("\n===== 🔐 PRIVACY & SECURITY PAGE OPENED =====");
        }
        catch(Exception e){
            System.out.println("❌ Unable to open Privacy & Security page");
        }
    }

    // ================= VERIFY CONTENT =================
    public void verifySecurityBadges(){

        safeVerify("GDPR Compliance", get("privacy.gdpr"));
        safeVerify("HIPAA Compliance", get("privacy.hipaa"));
        safeVerify("ISO 27001", get("privacy.iso"));
    }

    // ================= LEGAL DOCUMENTS =================
    public void openLegalDocuments() throws InterruptedException {

        try{
            WebElement legal =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(get("privacy.legal.section")));

            ((JavascriptExecutor)driver)
                    .executeScript("arguments[0].scrollIntoView(true);", legal);

            Thread.sleep(800);
            System.out.println("📜 Scrolled to Legal Documents");
        }
        catch(Exception e){
            System.out.println("⚠️ Legal section not found");
        }

        safeClick("Privacy Policy", get("privacy.policy.link"));
//        driver.navigate().back();
        Thread.sleep(800);

        safeClick("Terms & Conditions", get("privacy.terms.link"));
//        driver.navigate().back();
        Thread.sleep(800);
    }

    // ================= PREFERENCES =================
    public void handlePreferences() throws InterruptedException {

        try{
            WebElement pref =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(get("privacy.preferences.section")));

            ((JavascriptExecutor)driver)
                    .executeScript("arguments[0].scrollIntoView(true);", pref);

            Thread.sleep(800);
            System.out.println("⚙️ Scrolled to Preferences");
        }
        catch(Exception e){
            System.out.println("⚠️ Preferences section not visible");
        }

        toggleSwitch("Marketing Communications", get("privacy.marketing.toggle"));
        toggleSwitch("Third-Party Data Sharing", get("privacy.thirdparty.toggle"));
        toggleSwitch("Data Export Option", get("privacy.export.toggle"));
    }
}
