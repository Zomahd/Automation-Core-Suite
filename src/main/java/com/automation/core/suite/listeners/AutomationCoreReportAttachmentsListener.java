package com.automation.core.suite.listeners;

import com.automation.core.suite.AutomationCoreBase;
import com.automation.core.suite.AutomationCoreTest;
import com.automation.core.suite.utils.AutomationCoreReportUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AutomationCoreReportAttachmentsListener implements ITestListener {


    public void onTestStart(ITestResult result) {
        Object instance = result.getInstance();

        if (instance instanceof AutomationCoreTest) {
            result.setAttribute("browser", ((AutomationCoreTest) instance).getBrowser());
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
        context.setAttribute("executionName", AutomationCoreReportUtils.getExecutionTagName());
    }

    public void onFinish(ITestContext context) {}


    protected void processAttachments(ITestResult result) {
        Object instance = result.getInstance();

        if (instance instanceof AutomationCoreBase) {
            AutomationCoreBase test = (AutomationCoreBase) instance;
            Throwable exception = result.getThrowable();

            result.setAttribute("logger", test.getLog());
            result.setAttribute("allEvents", test.getAllEvents());

            if (instance instanceof AutomationCoreTest) {
                result.setAttribute("screenshots", ((AutomationCoreTest) instance).getScreenshots());
            }

            if (exception != null) {
                result.setAttribute("exception", ExceptionUtils.getFullStackTrace(exception));
            }
        }
    }
}
