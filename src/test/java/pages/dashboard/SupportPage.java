package pages.dashboard;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import config.ConfigLoader;
import java.time.Duration;
import java.util.List;

public class SupportPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public SupportPage(WebDriver driver){
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
    
    

    // ================= OPEN SUPPORT =================
    public void openSupport(){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(get("support.menu"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("support.page.header")));
            System.out.println("\n===== 🆘 HELP & SUPPORT PAGE OPENED =====");
        }catch(Exception e){
            System.out.println("❌ Support page not accessible");
        }
    }

    // ================= CHAT =================
    public void verifyChatSupport(){
        safeClick("Chat with Admin", get("support.chat.button"));

        try{
            WebElement box = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(get("support.chat.box"))
            );
            box.sendKeys(ConfigLoader.get("support.chat.message"));

            safeClick("Send Chat Message", get("support.chat.send"));
            System.out.println("💬 Chat message sent");

        }catch(Exception e){
            System.out.println("⚠️ Chat not available or disabled");
        }
    }

    // ================= CALL US =================
    public void verifyCallUs(){
        safeClick("Call Us", get("support.call.button"));

        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("support.call.popup")));
            WebElement number =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(get("support.call.number")));

            System.out.println("📞 Call Support Number → " + number.getText());
        }
        catch(Exception e){
            System.out.println("⚠️ Call info not visible");
        }
    }

    // ================= EMAIL SUPPORT =================
    public void verifyEmailSupport(){
        safeClick("Email Support", get("support.email.button"));

        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(get("support.email.popup")));

            Select priority =
                    new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(
                            get("support.email.priority"))));
            priority.selectByVisibleText(ConfigLoader.get("support.email.priority.value"));

            driver.findElement(get("support.email.subject"))
                    .sendKeys(ConfigLoader.get("support.email.subject.value"));

            driver.findElement(get("support.email.message"))
                    .sendKeys(ConfigLoader.get("support.email.message.value"));

            safeClick("Send Email", get("support.email.send"));

            System.out.println("📧 Support email submitted");

        }catch(Exception e){
            System.out.println("⚠️ Email support form not available");
        }
    }

    // ================= FAQ =================
    public void verifyFAQ() throws InterruptedException {

        safeClick("FAQ", get("support.faq.button"));

        List<WebElement> questions;

        try{
            questions = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(
                            get("support.faq.questions"))
            );
        }catch(Exception e){
            System.out.println("⚠️ No FAQ questions found");
            return;
        }

        System.out.println("📚 FAQ Questions Found → " + questions.size());

        int index = 1;

        for(WebElement q : questions){
            try{
                ((JavascriptExecutor)driver)
                        .executeScript("arguments[0].scrollIntoView({block:'center'});", q);

                q.click();
                Thread.sleep(500);

                System.out.println("❓ FAQ Opened #" + index);
            }
            catch(Exception e){
                System.out.println("⚠️ Unable to open FAQ #" + index);
            }
            index++;
        }
    }
}
