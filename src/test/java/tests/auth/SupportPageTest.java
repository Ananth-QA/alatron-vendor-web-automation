package tests.auth;

import org.testng.annotations.Test;

import pages.dashboard.SettingsPage;
import pages.dashboard.SupportPage;
import base.CookieloginBypass;

public class SupportPageTest extends CookieloginBypass {
	
	@Test
	public void verifyHelpAndSupportFlow() throws Exception {

	    System.out.println("\n🚀 HELP & SUPPORT FLOW STARTED");

	    SupportPage support = new SupportPage(driver);
	    
	    support.clickConfirmation();
	    
	    support.openProfile();

	    support.openSupport();

	    support.verifyChatSupport();

	    support.verifyCallUs();

	    support.verifyEmailSupport();

	    support.verifyFAQ();

	    System.out.println("\n🎯 HELP & SUPPORT FLOW COMPLETED SUCCESSFULLY");
	}

	
}