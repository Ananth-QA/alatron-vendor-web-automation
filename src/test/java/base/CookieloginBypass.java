package base;

import config.ConfigLoader;

import drivers.DriverFactory;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

public class CookieloginBypass {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {

        driver = DriverFactory.initDriver();
        driver.manage().window().maximize();

        driver.get(ConfigLoader.get("base.url"));

        Cookie sessionCookie = new Cookie.Builder(
                "__Secure-next-auth.session-token",
                "eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIn0..BUOPi6nK3UtpYINK.Bfuls14vgohLsG5te1kQAcykq992dXhiUFnn4xrM4L3WLbH4g976rJDmUSjAdLy8xibpjjwHZ_aKHFvTKzHRevUQf2YfnurKagakYA5832a7KGWm_Z-QI-cElHaJUOCOswvrlMHts-Lt0uLkC7MOwVp_G7nIjBkKWC4j9zSMxDRCbrh2zakdKfA4Na9fxb8rdi9QpNkuWD88LPCrdn9Vg0wIMjfQpQUpOHWkrQRaYxFmt-QOIZkpFApyAdefRTZ78HrgY9Ezvywcre_bnW6z6778zCrvAyztZe2whEOR_KVvH4ORUVYmfzJRyXjXA1UFFifHEu0I31esaGFhq7f8YlCeYSlcIYdwwMV4H80Xpv51SdhqGE52wjrL0UepmmyYG8vOv8Fr19YDgUyOArzOHhPZnlF4fJVPLgP9n0ZW8adWxGhvZvpakw2q2OQO4zJ8vgiezrqisicXYc8FNZ3v1XA1YkVRH-YqX-K_72kVLa1TPMxAWM1BkYEwFm80mKt5DOl71HQ_rqw_0V7iV2yhooz9mqED86gMF6xczcoEAnKCoQW2MUrsuRxqoZqb7BWvDky6hgu-C1Plj7mL-ESlawZIxbQcFeNZJLLCkc75A4WxHxQGFq-HYyHKOYneYzxb_Yo6DByWw9A6TWbQFzcLZIRYLcQMpBO3SIk1yVU.9U7E4Kk839lrl1wME89kCQ")
                .domain("vendor.alatron.id")
                .path("/")
                .isSecure(true)
                .isHttpOnly(true)
                .build();

        driver.manage().addCookie(sessionCookie);
        driver.navigate().refresh();

        System.out.println("✅ Logged in via Cookie Bypass");
        
    }

//    @AfterMethod
//    public void tearDown() {
//        if(driver != null){
//            driver.quit();
//        }
//    }
}



