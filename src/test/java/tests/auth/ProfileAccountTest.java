package tests.auth;

import org.testng.annotations.Test;

import pages.dashboard.ProfileAccountPage;
import pages.dashboard.ProfilePage;
import base.BaseTest;
import base.CookieloginBypass;

public class ProfileAccountTest extends CookieloginBypass {

	@Test
	public void verifyAccountManagementFlow() throws Exception {

	    System.out.println("\n🚀 STARTING ACCOUNT MANAGEMENT VALIDATION FLOW");

	    ProfileAccountPage acc = new ProfileAccountPage(driver);
	   
	    acc.clickConfirmation();
	    
	    acc.openProfile();
	    
	    acc.openAccountManagement();
	    
	    acc.verifyMyGears();

	    acc.verifyMyRentals();

	    acc.verifyInvoiceHistory();

	    acc.verifyEarnings();

	    acc.verifyAccountCreation();

	    System.out.println("\n🎯 ACCOUNT MANAGEMENT TEST COMPLETED");
	}

}