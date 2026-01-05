package pages.dashboard;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import config.ConfigLoader;
import java.time.Duration;
import java.util.List;

public class NotificationPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public NotificationPage(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    private By get(String key){
        return By.xpath(ConfigLoader.get(key));
    }

    // ================= OPEN NOTIFICATION PAGE =================
    public void openNotifications(){

        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("notification.menu"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("notification.page.header")));

            System.out.println("\n===== 🔔 NOTIFICATIONS PAGE OPENED =====");
        }
        catch(Exception e){
            System.out.println("❌ Unable to open Notifications page");
        }
    }

    // ================= TAB SWITCHING =================
    public void verifyAllTabs(){

        List<WebElement> tabs;

        try{
            tabs = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(
                            get("notification.tabs"))
            );
        }
        catch(Exception e){
            System.out.println("❌ Notification tabs not found");
            return;
        }

        System.out.println("📌 Total Tabs Found: " + tabs.size());

        int index = 1;

        for(WebElement tab : tabs){

            try{
                String tabName = tab.getText().trim();

                wait.until(ExpectedConditions.elementToBeClickable(tab)).click();
                Thread.sleep(900); // allow content refresh

                System.out.println("\n➡️ TAB " + index + " SELECTED → " + tabName);

                verifyNotificationsForTab(tabName);

            }
            catch(Exception e){
                System.out.println("⚠️ Failed switching tab #" + index);
            }

            index++;
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
    
    

    // ================= NOTIFICATION VERIFICATION =================
    private void verifyNotificationsForTab(String tabName){

        try{
            List<WebElement> cards = driver.findElements(get("notification.card"));

            if(cards.size() == 0){
                checkEmptyState(tabName);
                return;
            }

            System.out.println("🟢 Notifications Found in [" + tabName + "] → " + cards.size());

            // Print first notification safely
            WebElement card = cards.get(0);

            try{
                System.out.println("🔔 Title → "
                        + card.findElement(get("notification.title")).getText());
            }catch(Exception ignored){}

            try{
                System.out.println("⏱ Time → "
                        + card.findElement(get("notification.time")).getText());
            }catch(Exception ignored){}

            try{
                System.out.println("📝 Description → "
                        + card.findElement(get("notification.desc")).getText());
            }catch(Exception ignored){}

        }
        catch(Exception e){
            System.out.println("⚠️ Unable to read notification cards for " + tabName);
        }
    }

    // ================= EMPTY STATE =================
    private void checkEmptyState(String tabName){
        try{
            driver.findElement(get("notification.empty"));
            System.out.println("ℹ️ [" + tabName + "] No Notifications Available");
        }
        catch(Exception e){
            System.out.println("ℹ️ [" + tabName + "] No cards & no empty message shown");
        }
    }
}
