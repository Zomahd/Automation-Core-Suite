package com.accenture.dpcs.hero.listeners;

import com.accenture.dpcs.hero.HeroBase;
import com.accenture.dpcs.hero.HeroTest;
import com.accenture.dpcs.hero.utils.HeroReportUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class HeroReportAttachmentsListener implements ITestListener {


    public void onTestStart(ITestResult result) {
        Object instance = result.getInstance();

        if (instance instanceof HeroTest) {
            result.setAttribute("browser", ((HeroTest) instance).getBrowser());
        }
    }

    public void onTestSuccess(ITestResult result) {
        processAttachments(result);
    }

    public void onTestFailure(ITestResult result) {
        processAttachments(result);
    }

    public void onTestSkipped(ITestResult result) {}

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        processAttachments(result);
    }

    public void onStart(ITestContext context) {
        context.setAttribute("executionName", HeroReportUtils.getExecutionTagName());
    }

    public void onFinish(ITestContext context) {}


    protected void processAttachments(ITestResult result) {
        Object instance = result.getInstance();

        if (instance instanceof HeroBase) {
            HeroBase test = (HeroBase) instance;
            Throwable exception = result.getThrowable();

            result.setAttribute("logger", test.getLog());
            result.setAttribute("allEvents", test.getAllEvents());

            if (instance instanceof HeroTest) {
                result.setAttribute("screenshots", ((HeroTest) instance).getScreenshots());
            }

            if (exception != null) {
                result.setAttribute("exception", ExceptionUtils.getFullStackTrace(exception));
            }
        }
    }
}
