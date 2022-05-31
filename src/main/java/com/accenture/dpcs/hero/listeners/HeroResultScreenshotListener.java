package com.accenture.dpcs.hero.listeners;

import com.accenture.dpcs.hero.HeroTest;
import com.accenture.dpcs.hero.annotations.Screenshot;
import com.accenture.dpcs.hero.utils.HeroReportUtils;
import com.accenture.dpcs.hero.utils.HeroUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class HeroResultScreenshotListener implements ITestListener {

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

        if (instance instanceof HeroTest) {
            HeroTest test = (HeroTest) instance;

            screenshot = HeroUtils.getMethodAnnotation(HeroReportUtils.getMethod(result), Screenshot.class);

            if (screenshot == null) {
                screenshot = HeroUtils.getClassAnnotation(instance, Screenshot.class);
            }

            if (screenshot != null) {
                test.screenshot(screenshot.name());
            }
        }
    }
}
