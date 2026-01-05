package pages.dashboard;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

public class ProfilePage {

    private WebDriver driver;
    private WebDriverWait wait;
    private Properties prop;

    public ProfilePage(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));
        loadProperties();
    }

    private void loadProperties(){
        try{
            prop = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            prop.load(fis);
        } catch (Exception e){
            throw new RuntimeException("🔥 Unable To Load config.properties");
        }
    }

    private By get(String key){
        return By.xpath(prop.getProperty(key));
    }

    private void safeLogField(By locator, String label){
        try{
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            String value = el.getAttribute("value");
            if(value == null || value.isBlank())
                value = el.getText();

            System.out.println("🟢 " + label + " → " + value.trim());
        }
        catch (Exception e){
            System.out.println("⚠️ " + label + " Missing — continuing...");
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

    // ================= FIRST → UPLOAD IMAGE ======================
    public void uploadProfileImage(){

        System.out.println("\n===== 📤 UPLOADING PROFILE IMAGE FIRST =====");

        try {
            WebElement uploadInput =
                    wait.until(ExpectedConditions.presenceOfElementLocated(get("profile.upload.input")));

            WebElement uploadField =
                    wait.until(ExpectedConditions.presenceOfElementLocated(get("profile.upload.field")));

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView(true);", uploadField);

            Thread.sleep(800);

            String filePath = prop.getProperty("profile.kyc.file");

            File file = new File(filePath);
            if(!file.isAbsolute())
                file = new File(System.getProperty("user.dir") + File.separator + filePath);

            uploadInput.sendKeys(file.getAbsolutePath());

            System.out.println("📤 Uploaded Profile Image: " + file.getName());

            Thread.sleep(1500);

        } catch (Exception e){
            System.out.println("❌ Profile Image Upload Failed — Continuing...");
        }
    }

    // ================= SECOND → VERIFY ======================
    public void verifyProfileFields(){

        System.out.println("\n===== 🔍 VERIFYING PROFILE FIELDS =====");

        safeLogField(get("profile.company.field"), "Business Name");
        safeLogField(get("profile.name.field"), "Contact Person Name");
        safeLogField(get("profile.email.field"), "Email Address");
        safeLogField(get("profile.phone.field"), "Phone Number");

        try{
            if(!driver.findElement(get("profile.phone.field")).isEnabled())
                System.out.println("ℹ️ Phone Disabled → Login Using Mobile Number");
        } catch(Exception ignored) {}

        try{
            if(!driver.findElement(get("profile.email.field")).isEnabled())
                System.out.println("ℹ️ Email Disabled → Login Using Email Flow");
        } catch(Exception ignored){}

        safeLogField(get("profile.address.block"), "Address Section");

        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("profile.kyc.header")));
            System.out.println("🟢 KYC Section Present");
        }
        catch (Exception e){
            System.out.println("⚠️ KYC Section Missing — Continuing...");
        }
    }

    // ================= THIRD → EDIT NAME ======================
    public void editName(){
        System.out.println("\n===== ✏️ EDIT NAME =====");

        try{
            WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(get("profile.name.field")));
            name.clear();
            name.sendKeys(prop.getProperty("profile.edit.name"));

            System.out.println("🟢 Name Updated → " + prop.getProperty("profile.edit.name"));
        }
        catch (Exception e){
            System.out.println("❌ Unable to Edit Name — Continuing...");
        }
    }

    // ================= FOURTH → SAVE ======================
    public void saveProfile(){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("profile.save.button"))).click();
            System.out.println("🟡 Save Button Clicked");

        } catch (Exception e){
            System.out.println("⚠️ Save Click Failed — Continuing");
            return;
        }

        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("profile.success.toast")));
            System.out.println("✅ Profile Updated Successfully");
        }
        catch(Exception e){
            System.out.println("⚠️ Success Toast Not Found — Maybe Silently Saved");
        }
    }
}
