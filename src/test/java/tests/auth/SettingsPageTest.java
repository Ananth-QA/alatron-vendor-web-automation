package tests.auth;

import org.testng.annotations.Test;

import pages.dashboard.SettingsPage;
import base.CookieloginBypass;

public class SettingsPageTest extends CookieloginBypass {

	
	@Test
	public void verifySettingsFlow() throws Exception {

	    System.out.println("\n🚀 SETTINGS FLOW STARTED");

	    SettingsPage settings = new SettingsPage(driver);
	    
	    settings.clickConfirmation();
	    
	    settings.openProfile();

	    settings.openSettings();

	    settings.verifyEditProfile();

	    settings.handleLanguage();

	    settings.verifyAppVersion();

	    settings.verifyDeleteAccount();

	    settings.verifyLogout();

	    System.out.println("\n🎯 SETTINGS FLOW COMPLETED SUCCESSFULLY");
	}

	
}