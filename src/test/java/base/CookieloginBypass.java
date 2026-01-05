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
                "eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIn0..jk0YCuPR4xgPbwJo.64OGon6XdPNj_uiLXoVWT6a6fgtdsV9FEXDePKvJispUDHsTQHO42V-DN7muJ3P1w3GFjzJtm8cpSa2RfCooDZU0YGCioPTg7HCBp9Ol4gH7e_f2b2F6eLJ6gU3wRn4cCUXnT81TdTFKgBSU7JKRB6JQhCys3gM3yfUz4OhoZMzw5bmhjU3w7Bs6tn-bJBYa_VCke4TI1JPchT3TcFX4fa6V4xNxL-HpLfzOe2iVuvOzEjAWK2GZEe1q8mpBMpaqhj3w2bRGEP_fSO10KKcp3vX0PYnCO_xW0OWV2ddQizHj-7n1KrkFW-0SYDDI35sRT6sfIdCR62g5-lGEilc_d3nZw51p9sYI_4LDYNp4Ixsu4CnGA7Gltdl-U1jeBIkPbgx6Vill41N1ia32iFlu9-9AaD0QYk8LJzbX05IXRIx9rwXYK2LxlOjZpGE3e2P3wJxM2lEKtvPsHdf07gSpzAJmwXdomdz3dK_nLTbupCXf6WWtEa8-GXTgIv2Lx9FAqqjEVoLIRXVsDOABXvWFTrQpYgif113W775TZS13CtDSKqnjxbqXqW281y9T9mRvw8GctghTr4EzXyFpsE2OXLR3DxEbk5gMhXapqO5aY4slUV6BOX0M12TEozeu4v4xOGWzD1TROQ_h7ic3AyYujhiF1fvNFV5BpyYdkrY.ok1xf-T1eLQAQM28FgycyA")
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



