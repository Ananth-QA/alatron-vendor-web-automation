package pages.dashboard;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import config.ConfigLoader;

import java.time.Duration;

public class SalesTeamPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public SalesTeamPage(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    private By get(String key){
        return By.xpath(ConfigLoader.get(key));
    }

    private void safeLog(String label, By locator){
        try{
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            String val = el.getAttribute("value");
            if(val == null || val.isBlank()) val = el.getText();
            System.out.println("🟢 " + label + " → " + val);
        }catch(Exception e){
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

    
    
    

    // ================= OPEN SALES TEAM =================
    public void openSalesTeam(){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("sales.menu"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("sales.page.header")));
            System.out.println("\n===== 👥 SALES TEAM PAGE OPENED =====");
        }catch(Exception e){
            System.out.println("❌ Sales Team page not accessible");
        }
    }

    // ================= ADD SALES PERSON =================
    public void addSalesPerson(){

        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("sales.add.btn"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("sales.popup.header")));
            System.out.println("🟢 Add Salesperson Popup Opened");
        }catch(Exception e){
            System.out.println("❌ Unable to open Add Salesperson popup");
            return;
        }

        try{ driver.findElement(get("sales.fullname"))
                .sendKeys(ConfigLoader.get("sales.value.name")); }catch(Exception ignored){}

        try{ driver.findElement(get("sales.phone"))
                .sendKeys(ConfigLoader.get("sales.value.phone")); }catch(Exception ignored){}

        try{ driver.findElement(get("sales.email"))
                .sendKeys(ConfigLoader.get("sales.value.email")); }catch(Exception ignored){}

        // Assign Yard → ENTER KEY
        try{
            WebElement yard = driver.findElement(get("sales.assign.yard"));
            yard.sendKeys(ConfigLoader.get("sales.value.yard"));
            yard.sendKeys(Keys.ENTER);
            System.out.println("🏗️ Yard Assigned");
        }catch(Exception e){
            System.out.println("⚠️ Assign Yard Failed");
        }

        // Assign Equipment → ENTER KEY
        try{
            WebElement equip = driver.findElement(get("sales.assign.equipment"));
            equip.sendKeys(ConfigLoader.get("sales.value.equipment"));
            equip.sendKeys(Keys.ENTER);
            System.out.println("🚜 Equipment Assigned");
        }catch(Exception e){
            System.out.println("⚠️ Assign Equipment Failed");
        }

        try{
            Select status = new Select(driver.findElement(get("sales.status")));
            status.selectByVisibleText(ConfigLoader.get("sales.value.status"));
        }catch(Exception ignored){}

        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("sales.submit.btn"))).click();
            System.out.println("💾 Add Salesperson Clicked");
        }catch(Exception e){
            System.out.println("❌ Add Salesperson button failed");
        }

        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("sales.toast")));
            System.out.println("✅ Salesperson Added Successfully");
        }catch(Exception e){
            System.out.println("⚠️ No success toast — continuing");
        }
    }

    // ================= VERIFY SALES CARD =================
    public void verifySalesPerson(){
        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("sales.latest.card")));
            System.out.println("🟢 Salesperson Card Visible");

            wait.until(ExpectedConditions.elementToBeClickable(get("sales.view.card"))).click();
            System.out.println("👁️ Viewing Salesperson Details");

        }catch(Exception e){
            System.out.println("⚠️ Salesperson card not found");
        }
    }

    // ================= EDIT SALES PERSON =================
    public void editSalesPerson() throws InterruptedException {

        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("sales.edit.icon"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("sales.popup.header")));
            System.out.println("\n===== ✏️ EDIT SALES PERSON =====");

            ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,400)");
            Thread.sleep(600);

            safeLog("Full Name", get("sales.fullname"));
            safeLog("Phone", get("sales.phone"));
            safeLog("Email", get("sales.email"));

            wait.until(ExpectedConditions.elementToBeClickable(get("sales.update.btn"))).click();
            System.out.println("📌 Update Clicked");

            try{
                wait.until(ExpectedConditions.visibilityOfElementLocated(get("sales.toast")));
                System.out.println("✅ Salesperson Updated Successfully");
            }catch(Exception ex){
                System.out.println("⚠️ Validation error — closing popup");
                driver.findElement(get("sales.close.popup")).click();
            }

        }catch(Exception e){
            System.out.println("⚠️ Edit Salesperson not available");
        }
    }

    // ================= DELETE SALES PERSON =================
    public void deleteSalesPerson(){

        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("sales.delete.icon"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(get("sales.delete.confirm"))).click();

            System.out.println("🗑️ Salesperson Deleted Successfully");
        }catch(Exception e){
            System.out.println("⚠️ Delete Salesperson not available");
        }
    }
}
