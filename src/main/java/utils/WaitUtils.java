package utils;

import constants.FrameworkConstants;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class WaitUtils {

    public static WebElement waitForVisible(WebDriver driver, By locator) {
        return new WebDriverWait(driver,
                java.time.Duration.ofSeconds(FrameworkConstants.EXPLICIT_WAIT))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
