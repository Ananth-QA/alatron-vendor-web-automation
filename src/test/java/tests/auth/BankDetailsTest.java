package tests.auth;

import org.testng.annotations.Test;

import pages.dashboard.BankDetailsPage;
import base.CookieloginBypass;

public class BankDetailsTest extends CookieloginBypass {
	
	@Test
	public void verifyBankDetailsFlow() throws InterruptedException{

	    System.out.println("\n🚀 PAYMENT & BANK DETAILS FLOW STARTED");

	    BankDetailsPage bank = new BankDetailsPage(driver);
	    
	    bank.clickConfirmation();
	    
	    bank.openProfile();

	    bank.openBankSection();

	    bank.fillBankDetailsIfEmpty();

	    bank.verifyExistingBankDetails();

	    System.out.println("\n🎯 PAYMENT & BANK DETAILS FLOW COMPLETED");
	}

}