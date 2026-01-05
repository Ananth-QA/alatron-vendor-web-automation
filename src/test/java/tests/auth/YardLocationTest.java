package tests.auth;

import org.testng.annotations.Test;

import pages.dashboard.YardLocationPage;
import base.CookieloginBypass;

public class YardLocationTest extends CookieloginBypass {

	@Test
	public void verifyYardLocationFlow() throws Exception {

	    System.out.println("\n🚀 YARD LOCATION MANAGEMENT FLOW STARTED");

	    YardLocationPage yard = new YardLocationPage(driver);
	    
	    yard.clickConfirmation();
	    
	    yard.openProfile();

	    yard.openYardPage();

	    yard.openAddPopup();

	    yard.fillYardForm();

	    yard.verifyNewLocation();

	    yard.editLocation();

	    yard.deleteLocation();

	    System.out.println("\n🎯 YARD LOCATION FLOW COMPLETED SUCCESSFULLY");
	}

	
}
	