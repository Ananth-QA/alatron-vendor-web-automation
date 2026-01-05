package tests.auth;

import org.testng.annotations.Test;

import pages.dashboard.PrivacySecurityPage;
import pages.dashboard.SalesTeamPage;
import pages.dashboard.YardLocationPage;
import base.CookieloginBypass;

public class PrivacySecurityTest extends CookieloginBypass {
	@Test
	public void verifyPrivacyAndSecurityFlow() throws Exception {

	    System.out.println("\n🚀 PRIVACY & SECURITY FLOW STARTED");

	    PrivacySecurityPage privacy = new PrivacySecurityPage(driver);
	    
	    privacy.clickConfirmation();
	    
	    privacy.openProfile();
	    
	    privacy.openPrivacySecurity();

	    privacy.verifySecurityBadges();

	    privacy.openLegalDocuments();
	    
	    privacy.handlePreferences();

	    System.out.println("\n🎯 PRIVACY & SECURITY FLOW COMPLETED SUCCESSFULLY");
	}


}
