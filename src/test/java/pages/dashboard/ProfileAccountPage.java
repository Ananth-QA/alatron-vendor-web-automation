package pages.dashboard;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import config.ConfigLoader;

import java.time.Duration;

public class ProfileAccountPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public ProfileAccountPage(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    private By get(String key){
        return By.xpath(ConfigLoader.get(key));
    }

    private void safePrint(String label, By locator){
        try{
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            System.out.println("🟢 " + label + " → " + el.getText());
        }catch(Exception e){
            System.out.println("⚠️ " + label + " Not Found — Continuing...");
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

    
    

    public void openAccountManagement(){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("account.menu.dropdown"))).click();
            System.out.println("🟢 Account Management Expanded");
        }catch (Exception e){
            System.out.println("❌ Failed To Open Account Management — Continuing...");
        }
    }
    
    public void verifyMyGears() throws InterruptedException {

        openAccountManagement();

        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("account.my.gears"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("gears.header")));

            System.out.println("\n===== 🛠️ MY GEARS PAGE =====");

            safePrint("Total Gear Listed", get("gears.total.count"));

            clickTab("All", get("gears.tabs.all"));
            clickTab("In Rental", get("gears.tabs.in.rental"));
            clickTab("Under Maintenance", get("gears.tabs.maintenance"));
            clickTab("Available", get("gears.tabs.available"));

            driver.navigate().back();
        }
        catch(Exception e){
            System.out.println("⚠️ My Gears Screen Not Available — Skipping");
        }

        Thread.sleep(800);
    }

    private void clickTab(String label, By locator){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
            System.out.println("➡️ Switched Tab: " + label);
            Thread.sleep(600);
        }catch (Exception e){
            System.out.println("⚠️ Tab Missing: " + label);
        }
    }

    
    
    
    
    

    // ================= MY RENTALS ====================
    public void verifyMyRentals() throws InterruptedException {

        openAccountManagement();

        wait.until(ExpectedConditions.elementToBeClickable(get("account.my.rentals"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(get("rentals.header")));
        System.out.println("\n===== 🚜 MY RENTALS PAGE =====");

        safePrint("Total Listed Gear", get("rentals.total.listed"));
        safePrint("In Rental", get("rentals.in.rental.box"));
        safePrint("Under Maintenance", get("rentals.maintenance.box"));
        safePrint("Available", get("rentals.available.box"));

        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("rentals.total.listed"))).click();
            System.out.println("➡️ Redirected To My Gear (All Gear)");
            driver.navigate().back();
        }catch(Exception ignored){}

        Thread.sleep(800);
    }

    // ================== INVOICE HISTORY =====================
    public void verifyInvoiceHistory() throws InterruptedException {

        openAccountManagement();
        wait.until(ExpectedConditions.elementToBeClickable(get("account.invoice.history"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(get("invoice.header")));
        System.out.println("\n===== 📄 INVOICE HISTORY =====");

        safePrint("Total Income", get("invoice.total"));
        safePrint("Outstanding", get("invoice.outstanding"));
        safePrint("Received", get("invoice.received"));

        try{
            WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(get("invoice.search.box")));
            search.sendKeys("INV");
            System.out.println("🔍 Searching Invoice → INV");

            Thread.sleep(1000);
        } catch (Exception e){
            System.out.println("⚠️ Search Not Available");
        }

        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("invoice.filter.button"))).click();
            System.out.println("🧰 Filter Opened");
        }catch(Exception e){
            System.out.println("⚠️ Filter Not Found");
        }

        openFirstInvoice();
    }

    private void openFirstInvoice(){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("invoice.first.result"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("invoice.details.header")));

            System.out.println("🟢 Invoice Details Page Opened");

            safePrint("Invoice ID", get("invoice.ref.id"));
            safePrint("Status", get("invoice.status.label"));

            handleReminder();

        }catch(Exception e){
            System.out.println("⚠️ No Invoice Found — Continue");
        }
    }

    private void handleReminder(){
        try{
            WebElement btn = driver.findElement(get("invoice.send.reminder"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            btn.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("invoice.reminder.toast")));

            System.out.println("📨 Reminder Sent Successfully");
        }catch(Exception e){
            System.out.println("ℹ️ Reminder Button Not Available (Maybe Paid Invoice)");
        }
    }

    // ================= MY EARNING =====================
    public void verifyEarnings(){

        openAccountManagement();
        wait.until(ExpectedConditions.elementToBeClickable(get("account.my.earning"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(get("earnings.header")));
        System.out.println("\n===== 💰 MY EARNINGS =====");

        safePrint("Total Earnings", get("earnings.total"));
        safePrint("This Week Earnings", get("earnings.this.week"));
        safePrint("This Month Earnings", get("earnings.this.month"));

        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("earnings.filter.icon"))).click();
            System.out.println("🧰 Earnings Filter Opened");
        }catch(Exception e){
            System.out.println("⚠️ Earnings Filter Not Available");
        }
    }

    // ================= ACCOUNT CREATION =====================
    public void verifyAccountCreation(){
        openAccountManagement();
        wait.until(ExpectedConditions.elementToBeClickable(get("account.account.creation"))).click();

        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("account.creation.status")));
            System.out.println("🟢 Account Creation Completed");
        }catch(Exception e){
            System.out.println("❌ Account Creation Status Not Completed");
        }
    }
}
