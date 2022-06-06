package com.automation.core.suite.reporters.extent;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.model.Log;
import com.relevantcodes.extentreports.model.Test;
import com.relevantcodes.extentreports.utils.ExceptionUtil;


@SuppressWarnings("unused")
public class AutomationCoreExtentTest extends ExtentTest {

    private LogStatus runStatus;

    public AutomationCoreExtentTest(String testName, String description) {
        super(testName, description);
        this.runStatus = LogStatus.UNKNOWN;
    }

    @Override
    public void log(LogStatus logStatus, String stepName, String details) {
        log(logStatus, stepName, details, null);
    }

    @Override
    public LogStatus getRunStatus() {
        return runStatus;
    }

    public void log(LogStatus logStatus, String details, Long timestamp) {
        this.log(logStatus, null, details, timestamp);
    }

    public void log(LogStatus logStatus, Throwable throwable, Long timestamp) {
        this.log(logStatus, null, "<pre>" + ExceptionUtil.createExceptionInfo(throwable, (Test) getTest()).getStackTrace() + "</pre>", timestamp);
    }

    public void log(LogStatus logStatus, String stepName, Throwable throwable, Long timestamp) {
        this.log(logStatus, stepName, "<pre>" + ExceptionUtil.createExceptionInfo(throwable, (Test) getTest()).getStackTrace() + "</pre>", timestamp);
    }

    public void log(LogStatus logStatus, String stepName, String details, Long timestamp) {
        Log evt = new Log();
        evt.setLogStatus(logStatus);
        evt.setStepName(stepName == null ? null : stepName.trim());
        evt.setDetails(details == null ? "" : details.trim());

        Test test = (Test) getTest();
        test.setLog(evt);
        test.trackLastRunStatus();
        runStatus = test.getStatus();
    }
}
