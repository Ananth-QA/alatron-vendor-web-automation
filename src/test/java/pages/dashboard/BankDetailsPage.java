package pages.dashboard;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import config.ConfigLoader;

import java.time.Duration;

public class BankDetailsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public BankDetailsPage(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    private By get(String key){
        return By.xpath(ConfigLoader.get(key));
    }
 
    
    
    
    
    
    
    
    private void logValue(String label, By locator){
        try{
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            String value = el.getAttribute("value");
            if(value == null || value.isBlank())
                value = el.getText();

            System.out.println("🟢 " + label + " → " + value.trim());
        }
        catch(Exception e){
            System.out.println("⚠️ " + label + " Missing — continue");
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

    
    

    public void openBankSection(){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("bank.menu"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("bank.page.header")));

            System.out.println("\n===== 🏦 PAYMENT & BANK DETAILS PAGE LOADED =====");
        }
        catch(Exception e){
            System.out.println("❌ Failed to open Bank Details page — stopping bank only");
        }
    }

    private boolean isFieldEmpty(By locator){
        try{
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            String value = el.getAttribute("value");
            return value == null || value.trim().isEmpty();
        }catch(Exception e){
            return true;
        }
    }

    public void fillBankDetailsIfEmpty() throws InterruptedException{

        boolean needFill =
                isFieldEmpty(get("bank.account.holder")) ||
                isFieldEmpty(get("bank.account.number")) ||
                isFieldEmpty(get("bank.branch.code"));

        if(!needFill){
            System.out.println("ℹ️ Bank Details Already Exist → Skipping Fill");
            return;
        }

        System.out.println("✍️ Filling Bank Details (First Time)");

        try{
            Select bankSelect =
                    new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(get("bank.name"))));
            bankSelect.selectByVisibleText(ConfigLoader.get("bank.value.name"));
            System.out.println("🏦 Bank Selected → " + ConfigLoader.get("bank.value.name"));
        }catch(Exception e){
            System.out.println("⚠️ Bank Dropdown Not Found");
        }

        try{
            driver.findElement(get("bank.account.holder"))
                    .sendKeys(ConfigLoader.get("bank.value.holder"));
        }catch(Exception ignored){}

        try{
            driver.findElement(get("bank.account.number"))
                    .sendKeys(ConfigLoader.get("bank.value.account"));
        }catch(Exception ignored){}

        try{
            driver.findElement(get("bank.branch.code"))
                    .sendKeys(ConfigLoader.get("bank.value.branch"));
        }catch(Exception ignored){}

        try{
            driver.findElement(get("bank.npwp"))
                    .sendKeys(ConfigLoader.get("bank.value.npwp"));
        }catch(Exception ignored){}

        Thread.sleep(3000);
        
        
        saveBankDetails();
    }

    public void saveBankDetails(){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("bank.save.button"))).click();
            System.out.println("💾 Save Bank Details Clicked");

            wait.until(ExpectedConditions.visibilityOfElementLocated(get("bank.success.toast")));
            System.out.println("✅ Bank Details Saved Successfully");

        }catch(Exception e){
            System.out.println("⚠️ Unable to Save Bank Details — UI may not show toast");
        }
    }

    public void verifyExistingBankDetails(){
        System.out.println("\n===== 🔍 VERIFYING SAVED BANK DETAILS =====");

        logValue("Bank Name", get("bank.name"));
        logValue("Account Holder", get("bank.account.holder"));
        logValue("Account Number", get("bank.account.number"));
        logValue("Branch / Swift", get("bank.branch.code"));
        logValue("NPWP", get("bank.npwp"));
    }
}
