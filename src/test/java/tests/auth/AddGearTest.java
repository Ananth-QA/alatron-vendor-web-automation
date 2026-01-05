package tests.auth;

import base.CookieloginBypass;

import org.testng.annotations.Test;
import pages.dashboard.AddGearPage;

public class AddGearTest extends CookieloginBypass{

    @Test
    public void verifyAddGearInfoStep() throws InterruptedException {

        AddGearPage add = new AddGearPage(driver);

        add.clickConfirmation();
        add.clickAddGearHeader();
        add.verifyInfoPage();
        add.fillBasicDetails();
        add.uploadImages();
        add.validateMaxFiveImagesAndProceed();
        
//        add.verifyProgress();
        add.fillSpecifications();
        add.selectAttachmentsAndTerrain();
        add.fillDeliveryDetails();

        add.fillDeliveryDetails();
        add.selectLocationUsingMap();
        add.uploadBrochure();
//        add.handleAdditionalInformationAndContinue();        
        
        
        
        add.fillPriceAndTerms();
        add.reviewAndSubmit();
        add.expandAdditionalInfoSections();
        add.reviewSubmitAndVerify();
        
        
    }
}
