package tests.auth;

import org.testng.annotations.Test;

import pages.dashboard.SalesTeamPage;
import pages.dashboard.YardLocationPage;
import base.CookieloginBypass;

public class SalesTeamTest extends CookieloginBypass {

	@Test
	public void verifySalesTeamFlow() throws Exception {

	    System.out.println("\n🚀 SALES TEAM MANAGEMENT FLOW STARTED");

	    SalesTeamPage sales = new SalesTeamPage(driver);
	    
	    sales.clickConfirmation();
	    
	    sales.openProfile();

	    sales.openSalesTeam();

	    sales.addSalesPerson();

	    sales.verifySalesPerson();

	    sales.editSalesPerson();

	    sales.deleteSalesPerson();

	    System.out.println("\n🎯 SALES TEAM FLOW COMPLETED SUCCESSFULLY");
	}

}
