package com.QWorks.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;

import com.relevantcodes.extentreports.LogStatus;
import com.utility.Constant;
import com.utility.ExcelUtils;

public class LoginPage extends Common {

	@Test
	public static void userLogin() throws Exception {

		ExcelUtils.setExcelFile(Constant.Path_TestData + Constant.File_TestData, "Sheet1");

		String email = ExcelUtils.getCellData(1, 1);
		String pw = ExcelUtils.getCellData(1, 2);

		// This is to get the values from Excel sheet, passing parameters (Row num &amp;
		// Col num)to getCellData method
		test = extent.startTest("User Login Validation");
		test.getTest().setDescription("Validate the Login with valid and invalid credentials.");

		Thread.sleep(100);

		new LoginPage().emailFieldValidation();

		driver.findElement(By
				.xpath("/html/body/div[1]/div[2]/main/div/div/div/div/div/div[1]/form/div[1]/div/div[1]/div[1]/input"))
				.sendKeys(email);

		Common.emailFormatValidation(email);
		Common.isValidEmail(email);

		new LoginPage().pwFieldValidation(pw);

		new LoginPage().pxEncriptionValidation();

		new LoginPage().rememberMe();

		new LoginPage().loginVerification(email);

		Thread.sleep(5000);

		// Take a screenshot
		Common.screenshot("image2");

		Thread.sleep(100);

		System.out.print("Done\n");
	}

	public void emailFieldValidation() throws InterruptedException, IOException {

		// Click Login button
		driver.findElement(By.xpath("//*[@id=\"appRoot\"]/div[2]/main/div/div/div/div/div/div[2]/button")).click();

		Thread.sleep(300);

		// Take a screenshot
		Common.screenshot("image1");

		// Check whether the email field is empty
		if (driver
				.findElement(By.xpath(
						"/html/body/div[1]/div[2]/main/div/div/div/div/div/div[1]/form/div[1]/div/div[1]/div[1]/input"))
				.getAttribute("value").isEmpty()) {
			System.out.println("Email field is Empty");

			String actualError = driver.findElement(By.xpath(
					"//*[@id=\"appRoot\"]/div[2]/main/div/div/div/div/div/div[1]/form/div[1]/div/div[2]/div/div/div"))
					.getText();
			String expectedError = "The Email field is required.";
			Assert.assertEquals(actualError, expectedError);

			System.out.println(actualError + " Testcase is passed.");
			test.log(LogStatus.PASS, "Email Field Mandotory Validation");

		} else {
			System.out.println("Email field is not empty");
			test.log(LogStatus.FAIL, "Email Field Mandotory Validation");
		}
	}

	public void pwFieldValidation(String pw) throws Exception {

		// Check whether the password field is empty
		if (driver.findElement(By.id("password")).getAttribute("value").isEmpty()) {
			System.out.println("Password field is Empty");

			String actualError = driver.findElement(By.xpath(
					"//*[@id=\"appRoot\"]/div[2]/main/div/div/div/div/div/div[1]/form/div[2]/div/div[2]/div/div/div"))
					.getText();
			String expectedError = "The password field is required.";
			Assert.assertEquals(actualError, expectedError);

			System.out.println(actualError + " Testcase is passed.");
			test.log(LogStatus.PASS, "Password Field Mandotory Validation");

		} else {
			System.out.println("Password field is not empty");
			test.log(LogStatus.FAIL, "Password Field Mandotory Validation");
		}

		Thread.sleep(100);

		driver.findElement(By.id("password")).sendKeys(pw);
		System.out.println("Password field has filled");

	}

	public void pxEncriptionValidation() {

		// Check whether the password is encrypted or not
		WebElement input = driver.findElement(By.id("password"));
		boolean isEncrypted = input.getAttribute("type").equals("password");

		if (isEncrypted == true) {
			System.out.println("Password field is Encrypted");
			test.log(LogStatus.PASS, "Password Field Encryption Validation");
		} else {
			System.out.println("Password field is Not encrypted");
			test.log(LogStatus.FAIL, "Password Field Encryption Validation");
		}

	}

	public void rememberMe() {

		// Check whether the 'Remember me' check box is checked or not
		if (driver
				.findElement(By.xpath(
						"/html/body/div[1]/div[2]/main/div/div/div/div/div/div[1]/form/div[3]/div/div[1]/div/div"))
				.isSelected()) {
			System.out.println("'Remember me' check box is selected by default");

		} else {
			System.out.println("'Remember me' check box is not selected by default");
			driver.findElement(
					By.xpath("/html/body/div[1]/div[2]/main/div/div/div/div/div/div[1]/form/div[3]/div/div[1]/div/div"))
					.click();
			System.out.println("'Remember me' check box is selected.");
			test.log(LogStatus.PASS, "'Remember me' chechbox selection");
		}
	}

	public void loginVerification(String email) throws Exception {

		// Click Login button
		driver.findElement(By.xpath("//button")).click();

		try {
			// validate successful user login
			String expectedlogin = "Welcome to QWorks (staging) - " + email;
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			Boolean iselementpresent = driver
					.findElements(By.xpath("//*[@id=\"app\"]/div/main/div/div/div/div/div[1]/h4")).size() > 0;
			System.out.println(iselementpresent);
			if (iselementpresent) {

				String actuallogin = driver.findElement(By.xpath("//*[@id=\"app\"]/div/main/div/div/div/div/div[1]/h4"))
						.getText();
				Assert.assertEquals(expectedlogin, actuallogin);
				System.out.println(actuallogin + "\nUser Login Success! test case is passed with valid credentials");
				test.log(LogStatus.PASS, "User Login Success with valid credentials");

				// This is to send the PASS value to the Excel sheet in the result column.
				ExcelUtils.setCellData("Pass", 1, 3);

			}

			// validate the displaying toast message
			else if (iselementpresent) {
				driver.manage().timeouts().implicitlyWait(150, TimeUnit.SECONDS);
				driver.findElement(By.xpath("//div[contains(text(),'Invalid Credentials! Please try again.')]"))
						.click();
				driver.switchTo().activeElement();
				String actualAlertlMsg = driver
						.findElement(By.xpath("//div[contains(text(),'Invalid Credentials! Please try again.')]"))
						.getText();

				String expectedAlertlMsg = "error_outline\nInvalid Credentials! Please try again.\nCLOSE";
				Assert.assertEquals(actualAlertlMsg, expectedAlertlMsg);

				System.out.println(actualAlertlMsg + "\nUser Login Fail! test case is passed with invalid credentials");
				test.log(LogStatus.PASS, "User Login Fail with invalid credentials");
				// This is to send the PASS value to the Excel sheet in the result column.
				ExcelUtils.setCellData("Pass", 1, 3);

			}

			else {

				driver.manage().timeouts().implicitlyWait(150, TimeUnit.SECONDS);

				// div[contains(text(),'User not found.')]
				driver.findElement(By
						.xpath(".//*[normalize-space(text()) and normalize-space(.)='Sign In'])[1]/following::div[3]"))
						.click();
				driver.switchTo().activeElement();
				String actualAlertlMsg1 = driver
						.findElement(By.xpath(
								".//*[normalize-space(text()) and normalize-space(.)='Sign In'])[1]/following::div[3]"))
						.getText();

				String expectedAlertlMsg1 = "error_outline\nUser not found.\nCLOSE";
				Assert.assertEquals(actualAlertlMsg1, expectedAlertlMsg1);

				// print results in console
				System.out.println("error_outline\nUser not found.\nCLOSE");

				// print results in html report
				test.log(LogStatus.PASS, "User Login Fail since email does not belongs to an existing account.");

				// print results in test data excel
				ExcelUtils.setCellData("Pass", 1, 3);

			}

		} catch (Throwable e) {
			System.err.println("Error came while waiting for the alert popup. " + e.getMessage());
			test.log(LogStatus.FAIL, "Error came while waiting for the alert popup." + e.getMessage());
			// This is to send the PASS value to the Excel sheet in the result column.
			ExcelUtils.setCellData("Fail", 1, 3);
		}

	}

}