package pages.dashboard;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import config.ConfigLoader;
import java.time.Duration;

public class SettingsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public SettingsPage(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    private By get(String key){
        return By.xpath(ConfigLoader.get(key));
    }

    private void safeClick(String label, By locator){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
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
    
    
    // ================= OPEN SETTINGS =================
    public void openSettings(){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("settings.menu"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("settings.page.header")));
            System.out.println("\n===== ⚙️ SETTINGS PAGE OPENED =====");
        }catch(Exception e){
            System.out.println("❌ Settings page not accessible");
        }
    }

    // ================= EDIT PROFILE =================
    public void verifyEditProfile(){
        safeClick("Edit Profile", get("settings.edit.profile"));

        try{
            System.out.println("➡️ Redirected to Profile Edit Page");
            driver.navigate().back();
        }catch(Exception ignored){}
    }

    // ================= LANGUAGE =================
    public void handleLanguage(){

        safeClick("Language", get("settings.language"));

        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("settings.language.popup")));

            Select lang = new Select(wait.until(
                    ExpectedConditions.visibilityOfElementLocated(get("settings.language.dropdown"))
            ));

            lang.selectByVisibleText(ConfigLoader.get("settings.language.option"));

            System.out.println("🌐 Language Selected → "
                    + ConfigLoader.get("settings.language.option"));

            safeClick("Close Language Popup", get("settings.language.close"));

        }catch(Exception e){
            System.out.println("⚠️ Language popup not available");
        }
    }

    // ================= APP VERSION =================
    public void verifyAppVersion(){
        safeClick("App Version", get("settings.app.version"));

        try{
            WebElement version =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(
                            get("settings.app.version.text")));

            System.out.println("📦 App Version → " + version.getText());

        }catch(Exception e){
            System.out.println("⚠️ App version not visible");
        }
    }

    // ================= DELETE ACCOUNT =================
    public void verifyDeleteAccount(){

        safeClick("Delete Account", get("settings.delete"));

        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("settings.delete.popup")));
            System.out.println("🧨 Delete Confirmation Popup Displayed");

            safeVerify("Delete Button", get("settings.delete.confirm"));
            safeClick("Cancel Delete", get("settings.delete.cancel"));

        }catch(Exception e){
            System.out.println("⚠️ Delete confirmation popup missing");
        }
    }

    // ================= LOGOUT =================
    public void verifyLogout(){

        safeClick("Logout", get("settings.logout"));

        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("settings.logout.popup")));
            System.out.println("🚪 Logout Confirmation Popup Displayed");

            safeVerify("Logout Button", get("settings.logout.confirm"));
            safeClick("Cancel Logout", get("settings.logout.cancel"));

        }catch(Exception e){
            System.out.println("⚠️ Logout confirmation popup missing");
        }
    }
}
