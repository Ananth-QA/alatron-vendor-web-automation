package tests.auth;

import org.testng.annotations.Test;
import pages.dashboard.ProfilePage;
import base.BaseTest;
import base.CookieloginBypass;

public class ProfileTest extends CookieloginBypass {

	@Test
	public void completeProfileFlow(){

	    System.out.println("\n🚀 PROFILE FLOW USING COOKIE LOGIN");

	    ProfilePage profile = new ProfilePage(driver);
	    
	    profile.clickConfirmation();

	    profile.openProfile();

	    profile.uploadProfileImage();

	    profile.verifyProfileFields();

	    profile.editName();

	    profile.saveProfile();

	    System.out.println("\n🎯 PROFILE FLOW COMPLETED SUCCESSFULLY");
	}

}
