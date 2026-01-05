package tests.auth;

import org.testng.annotations.Test;

import pages.dashboard.NotificationPage;

import base.CookieloginBypass;

public class NotificationTest extends CookieloginBypass {

	@Test
	public void verifyNotificationTabsFlow() throws Exception {

	    System.out.println("\n🚀 NOTIFICATION TAB FLOW STARTED");

	    NotificationPage notification = new NotificationPage(driver);
	    
	    notification.clickConfirmation();
	    
	    notification.openProfile();

	    notification.openNotifications();

	    notification.verifyAllTabs();

	    System.out.println("\n🎯 NOTIFICATION FLOW COMPLETED SUCCESSFULLY");
	}


}
