package com.QWorks.Test;

import static org.testng.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

public class PasswordReset extends Common {

	Common common = new Common();
	String email = "chamalika@dolphiq.com";

	@Test
	public void pwReset() throws Exception {

		test = extent.startTest("Password Reset Validation");
		test.getTest().setDescription("Validate the Password Reset with valid and invalid emails.");

		// Validate the presence of a 'Forgot password' link at the login page
		if (driver.findElement(By.xpath("//*[@id=\"appRoot\"]/div[2]/main/div/div/div/div/div/div[3]/div/p[1]/a"))
				.isDisplayed()) {

			System.out.println("Forgot Password link is Visible");
			test.log(LogStatus.PASS, "User redirection to Forgot password page");

		} else {
			System.out.println("Forgot Password link is not Visible");
			test.log(LogStatus.FAIL, "User redirection to Forgot password page");
		}

		Thread.sleep(30);

		new PasswordReset().resetPW();

		// take a screenshot
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		Common.screenshot("image5");

		Thread.sleep(30);

		// close the browser
		common.closeDriver();

	}

	public void resetPW() throws InterruptedException {

		// go to reset pw window at the login page
		driver.findElement(By.linkText("Reset here!")).click();
		Thread.sleep(80);

		driver.findElement(By.name("email")).click();
		Thread.sleep(500);

		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys(email);

		// validate the displaying toast message
		try {

			Common.emailFormatValidation(email);

			// click 'reset' button
			driver.findElement(By.cssSelector(".v-btn__content")).click();
			// Thread.sleep(100);

			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

			Boolean iselementpresent = driver
					.findElements(By.xpath("//div[contains(text(),'Password reset link has been sent.')]")).size() > 0;
			System.out.println(iselementpresent);

			if (iselementpresent) {

				driver.findElement(By.xpath("//div[contains(text(),'Password reset link has been sent.')]")).click();
				driver.switchTo().activeElement();
				String actualAlertlMsg = driver
						.findElement(By.xpath("//div[contains(text(),'Password reset link has been sent.')]"))
						.getText();

				String expectedAlertlMsg = "done\nPassword reset link has been sent.\nCLOSE";
				Assert.assertEquals(actualAlertlMsg, expectedAlertlMsg);

				System.out.println(actualAlertlMsg + "\nPassword reset test case is passed");

				test.log(LogStatus.PASS, "Validate the pw reset with a valid email");

			} else if (iselementpresent) {

				driver.findElement(By.xpath("//div[contains(text(),'something went wrong')]")).click();
				driver.switchTo().activeElement();
				String actualAlertlMsg1 = driver.findElement(By.xpath("//div[contains(text(),'something went wrong')]"))
						.getText();

				String expectedAlertlMsg1 = "error_outline\nOops, something went wrong\nCLOSE";
				Assert.assertEquals(actualAlertlMsg1, expectedAlertlMsg1);
				System.out.println(actualAlertlMsg1 + "\nPassword reset test case is passed");
				test.log(LogStatus.PASS, "Validate the pw reset with an invalid email");
			}

			else {
//				driver.findElement(By.xpath("//div[contains(text(),'user not found')]")).click();
//				driver.switchTo().activeElement();
//				String actualAlertlMsg2 = driver.findElement(By.xpath("//div[contains(text(),'user not found')]"))
//						.getText();
//
//				String expectedAlertlMsg2 = "error_outline\nUser not found\nCLOSE";
//				Assert.assertEquals(actualAlertlMsg2, expectedAlertlMsg2);

				assertEquals(driver.findElement(By
						.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Sign In'])[1]/following::div[3]"))
						.getText(), "error_outline\nUser not found.\nCLOSE");

				System.out.println( "\nPassword reset test case is passed");
				test.log(LogStatus.PASS, "Validate the pw reset with an email which doesn't have an user account");

			}

		} catch (Throwable e) {
			System.err.println("Error came while waiting for the alert popup. " + e.getMessage());
			test.log(LogStatus.FAIL, "Error came while waiting for the alert popup. \n " + e.getMessage());
		}

	}

}
