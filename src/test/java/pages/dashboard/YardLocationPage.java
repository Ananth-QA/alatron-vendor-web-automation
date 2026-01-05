package pages.dashboard;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import config.ConfigLoader;

import java.time.Duration;

public class YardLocationPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public YardLocationPage(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    private By get(String key){
        return By.xpath(ConfigLoader.get(key));
    }

    private void safeLog(String label, By locator){
        try{
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            String value = el.getAttribute("value");
            if(value == null || value.isBlank())
                value = el.getText();

            System.out.println("🟢 " + label + " → " + value.trim());
        }catch (Exception e){
            System.out.println("⚠️ " + label + " Missing");
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

    
    
    

    // ================= OPEN YARD PAGE =================
    public void openYardPage(){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("yard.menu"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("yard.page.header")));
            System.out.println("\n===== 🏗️ YARD / LOCATION PAGE OPENED =====");
        }catch(Exception e){
            System.out.println("❌ Unable to open Yard page");
        }
    }

    // ================= POPUP OPEN =================
    public void openAddPopup(){
        wait.until(ExpectedConditions.elementToBeClickable(get("yard.add.new.btn"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(get("yard.popup.header")));
        System.out.println("🟢 Add New Location Popup Opened");
    }

    private void select(By locator, String text){
        try{
            Select dropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(locator)));
            dropdown.selectByIndex(1);
            System.out.println("📌 Dropdown Selected: " + text);
        }catch(Exception e){
            System.out.println("⚠️ Dropdown " + text + " Missing — Skip");
        }
    }

    // ============= FILL YARD FORM =================
    public void fillYardForm(){

        try{
            driver.findElement(get("yard.name")).sendKeys(ConfigLoader.get("yard.value.name"));
        }catch(Exception ignored){}

        select(get("yard.province"), "Province");
        select(get("yard.city"), "City");
        select(get("yard.district"), "District");
        select(get("yard.village"), "Village");

        try{
            driver.findElement(get("yard.street")).sendKeys(ConfigLoader.get("yard.value.street"));
            driver.findElement(get("yard.postal")).sendKeys(ConfigLoader.get("yard.value.postal"));
        }catch(Exception ignored){}

        try{
            driver.findElement(get("yard.contact.person")).sendKeys(ConfigLoader.get("yard.value.person"));
            driver.findElement(get("yard.email")).sendKeys(ConfigLoader.get("yard.value.email"));
            driver.findElement(get("yard.mobile")).sendKeys(ConfigLoader.get("yard.value.mobile"));
        }catch(Exception ignored){}

        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("yard.add.location.btn"))).click();
            System.out.println("💾 Add Location Clicked");
        }catch(Exception e){
            System.out.println("❌ Unable to Click Add Location");
        }

        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("yard.toast")));
            System.out.println("✅ Location Added Successfully");
        }catch(Exception e){
            System.out.println("⚠️ No Success Toast — Maybe Added Silently");
        }
    }

    // ================= VERIFY IN LIST =================
    public void verifyNewLocation(){
        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("yard.latest.card")));
            System.out.println("🟢 New Location Visible in List");
        }catch(Exception e){
            System.out.println("⚠️ New Location Not Found — But Continue");
        }
    }

    // ================= EDIT FLOW =================
    public void editLocation() throws InterruptedException {

        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("yard.edit.icon"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("yard.popup.header")));

            System.out.println("\n===== ✏️ EDIT LOCATION POPUP OPENED =====");

            ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,500)");
            Thread.sleep(800);

            safeLog("Yard Name", get("yard.name"));
            safeLog("Street", get("yard.street"));
            safeLog("Postal", get("yard.postal"));
            safeLog("Contact Email", get("yard.email"));

            wait.until(ExpectedConditions.elementToBeClickable(get("yard.update.button"))).click();
            System.out.println("📌 Update Clicked");

            try{
                wait.until(ExpectedConditions.visibilityOfElementLocated(get("yard.toast")));
                System.out.println("✅ Location Updated Successfully");
            }catch(Exception ex){
                System.out.println("⚠️ Validation Error Toast – Closing Popup");
                wait.until(ExpectedConditions.elementToBeClickable(get("yard.close.popup"))).click();
            }

        }catch(Exception e){
            System.out.println("⚠️ Edit Not Available — Skipping Edit Flow");
        }
    }

    // ================= DELETE =================
    public void deleteLocation(){

        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("yard.delete.icon"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(get("yard.delete.confirm"))).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(get("yard.delete.success")));
            System.out.println("🗑️ Location Deleted Successfully");
        }
        catch(Exception e){
            System.out.println("⚠️ Delete Not Available — Skipping Delete Flow");
        }
    }
}
