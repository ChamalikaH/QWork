package com.QWorks.Test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Common {

	public static WebDriver driver;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static ExtentReports report;

	public static void screenshot(String fileName) throws IOException {
		// Take a screenshot
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		// Save the screenshot
		FileUtils.copyFile(scrFile,
				new File("//Users//macair//eclipse-workspace//DemoProject//Screenshots//" + fileName + ".png"));

	}


	@BeforeTest
	public static void chromeBrowser() throws Exception, Exception {

		System.setProperty("webdriver.chrome.driver", "//Users//macair//Documents//Automation_stuff//chromedriver");
		driver = new ChromeDriver();

		String baseUrl = "https://app.qwrks.test.vps20.dolphiq.eu/login";
		driver.manage().window().maximize();
		driver.get(baseUrl);

		return;
	}

	public void pwEncriptionValidation() {
		// Check whether the password is encrypted or not
		WebElement input = driver.findElement(By.id("password"));
		boolean isEncrypted = input.getAttribute("type").equals("password");

		if (isEncrypted == true) {
			System.out.println("Password field is Encrypted");
			test.log(LogStatus.PASS, "Password encryption validation");
		} else {
			System.out.println("Password field is Not encrypted");
			test.log(LogStatus.FAIL, "Password encryption validation");
		}

	}

	public void returnToSignInPage() {
		driver.findElement(By.linkText("Sign In")).click();

	}

	public static boolean emailFormatValidation(String email) {

		// Check whether the email is in correct format or not
		if (isValidEmail(email)) {
			System.out.println("Email is in the correct format");
			test.log(LogStatus.PASS, "Email format validation");

		} else {
			System.out.println("Email is not in the correct format");
			test.log(LogStatus.FAIL, "Email format validation");
		}

		return false;

	}

	protected static boolean isValidEmail(String email) {

		return true;
	}

	@AfterTest
	public void closeDriver() {

		driver.quit();
	}

	@BeforeSuite
	public void setup() {

		// report module initialization
		extent = new ExtentReports("//Users//macair//eclipse-workspace//DemoProject//Reports//TestResults.html", true);
	}

	@AfterSuite
	public void tearDown() {
		// update test information to the report
		extent.endTest(test);
		extent.flush();
	}

}
