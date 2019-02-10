package com.exonum.evoting.listeners;

import com.exonum.evoting.base.BaseTest;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestListener extends TestListenerAdapter {
    @Override
    public void onTestStart(ITestResult tr) {
        System.out.println(tr.getName() + " -- Test method started\n");
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        System.out.println(tr.getName() + " -- Test method failed\n");
        Object testClass = tr.getInstance();
        WebDriver driver = ((BaseTest) testClass).getWebDriver();
        if (driver != null) {
            System.out.println("Screenshot captured for test case: " + tr.getMethod().getMethodName());
            saveScreenshotPng(driver);
        }
        saveTextLog(tr.getMethod().getMethodName() + " failed and screenshot taken!");
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        System.out.println(tr.getName() + " -- Test method skipped\n");
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        System.out.println(tr.getName() + " -- Test method success\n");
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshotPng(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }
}
