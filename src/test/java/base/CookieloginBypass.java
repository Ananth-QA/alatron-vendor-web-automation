//package base;
//
//import org.openqa.selenium.Cookie;
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import java.time.Duration;
//
//public class CookieLoginBypass {
//
//    public static void main(String[] args) throws InterruptedException {
//
//        WebDriver driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        driver.manage().window().maximize();
//
//        // Open domain first (required before adding cookie)
//        driver.get("https://vendor.alatron.id/");
//
//        // Inject Session Cookie
//        Cookie sessionCookie = new Cookie.Builder(
//                "__Secure-next-auth.session-token",
//                "eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIn0..Ha8weQTHcpUOPc3Q.ThL96w0MtVGpCEZ_qdckKcqebYwV3-xhlkmWG5ELJ3vkt8TUx7qicQmYyYFBCdoi4FmRpbZyWhPQUW3zmhSnCJiG4vN5eeGn7HJ3v_YmnI177acrjFSTMWNALys-qDPO2o7RLAqNHj1MsG6dgtL-8IFiq9mySfRjqbVCJjeW3LN79QewWzIDdOp6itZtZxODa7FADQfhB7txgD_Kt2nSJI5RmS7-SjErO_JtAvM5xwHn_ux6B7pGG1f3rTrvqogv8E_fxqw3jvEontE43Jc1jttqLGanlHAOtsLj5INghx3LwbuYee3l7iKUt4g9Q3TED9ln4IP_f0zlSif657T9LtJUxoF-LV8Q94zgxCOeUVIN8-bDgUNe9iJlhKy6StVgm32upATVnnQq35hIPaFSTw2TBbhoKIbYHJo5VJPHyJvrxgO7uQPNkrtcBRtkGDQu33u3-TBiPqmkhvlEaMLUy4HDPSbaAeBbIL2KEpGJGa_zej7-LFHkYziik4x8Vcvsa2PCQsdcqVlUld29EJLx6gr-goHXNAa5p9WZfcMTPPd_4HGLtUr89GScO1vRPnbqmR6Q6IAmShLEqcZCfN2nx5GiwuTKGPyWekLeuA-IPKZ2dOcKoUTr24h2lFuMAzHJiSrDg3ZEgpasn05bQKLEgfGKEoefIRzVGIUjRzM.YQs_By4xRve9ODVY1t58Rg")
//                .domain("vendor.alatron.id")
//                .path("/")
//                .isSecure(true)
//                .isHttpOnly(true)
//                .build();
//
//        driver.manage().addCookie(sessionCookie);
//
//        // Refresh to apply cookie auth
//        driver.navigate().refresh();
//
//        System.out.println("✅ Logged in successfully via Cookie Bypass");
//
//        Thread.sleep(5000);
//        driver.quit();
//    }
//}


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
                "eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIn0..qoalL5FedqOxdZuF.MTtkDi8YIpzzzBlCDNC6FouUxPv4bRXUDXjm765bcYd5UhJ9tvieCyYi9onydz5_b2NOWk4iIQp8Dz2jQtaHuEARZIx2iB2lsnKraoyYvbTjpUNiQVS6f8DUlgM0AAffMaWPup1Vp1DtpXmD0hQMpoBtrCWkcnBICUT2UB9BtrPFF7XUIgE7eRY5Zz7LpDGsSLTRjtK25Pg8x0KQ-03FYjy2W6mOydLg44qaOo5dm1xq4IfoV4DXbYTgeGArHkrMvV0-YHoXJNo6majO8F95iHvrUsPoYCwCi9U7AYgLgEiWRvxUYJKWpnaM6VN0G4xNcwTldNx1Q8Gun0K1FFwMizKsG97b4femsokYmQRJk2sy2MvN-jse1ZFtaAt6KiJtjidZkHMJTFPye5qIfNIsuFDQUaT0ixxl5c_tYxSHv9OekO8hbxJxwPOT1QP7NtmLjHkAKV0GOm26KS56M41-trLw_No2ZBHAC9x5tPXY4XH1TwBqAtRLNaLEuUa9CA6l45_41U6hVctpp1hW8X1ID_DkmdT1OqxgOgVzBRUi4sjtPnj6vgqDoOdRC7UL3gdgxdnadg9njshTi5xiHBpSVOCECFNaV5b6VWdLCkvQzbvTOgiVLvJJA1vx1CMCDsuCrotHik-E179tajw23VszXiWmWGyY9G73OFGO4c4.HWBjsjgzwB35P3vgl3HuiA")
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



