package base;

import config.ConfigLoader;
import drivers.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.initDriver();
        driver.get(ConfigLoader.get("base.url"));
    }

    @AfterMethod
    public void tearDown() {
//        driver.quit();
    }
}
