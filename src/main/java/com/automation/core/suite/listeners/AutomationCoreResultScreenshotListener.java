package com.automation.core.suite.listeners;

import com.automation.core.suite.AutomationCoreTest;
import com.automation.core.suite.annotations.Screenshot;
import com.automation.core.suite.utils.AutomationCoreReportUtils;
import com.automation.core.suite.utils.AutomationCoreUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AutomationCoreResultScreenshotListener implements ITestListener {

    public void onTestStart(ITestResult result) {}

    public void onTestSuccess(ITestResult result) {
        takeScreenshot(result);
    }

    public void onTestFailure(ITestResult result) {
        takeScreenshot(result);
    }

    public void onTestSkipped(ITestResult result) {}

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        takeScreenshot(result);
    }

    public void onStart(ITestContext context) {}

    public void onFinish(ITestContext context) {}


    protected void takeScreenshot(ITestResult result) {
        Object instance = result.getInstance();
        Screenshot screenshot = null;

        if (instance instanceof AutomationCoreTest) {
            AutomationCoreTest test = (AutomationCoreTest) instance;

            screenshot = AutomationCoreUtils.getMethodAnnotation(AutomationCoreReportUtils.getMethod(result), Screenshot.class);

            if (screenshot == null) {
                screenshot = AutomationCoreUtils.getClassAnnotation(instance, Screenshot.class);
            }

            if (screenshot != null) {
                test.screenshot(screenshot.name());
            }
        }
    }
}
