package com.automation.core.suite.reporters.extent;

import com.automation.core.suite.annotations.SkipExtentReport;
import com.automation.core.suite.configuration.AutomationCoreConfigWrapper;
import com.automation.core.suite.logs.AutomationCoreLogRecord;
import com.automation.core.suite.models.Browser;
import com.automation.core.suite.models.Screenshot;
import com.automation.core.suite.models.Timestampable;
import com.automation.core.suite.utils.AutomationCoreReportUtils;
import com.automation.core.suite.utils.AutomationCoreUtils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.event.Level;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ExtentReporter implements IReporter {

    public final static String EXTENT_REPORT_DIRECTORY = "extent";

    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        String basePath = createReportDirectory(outputDirectory + EXTENT_REPORT_DIRECTORY +  File.separator);
        ExtentReports extent = new AutomationCoreExtentReports(basePath + File.separator + "index.html", true);
        extent.loadConfig(ExtentReporter.class.getResource("/extent-report.xml"));

        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();

            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();

                buildTestNodes(context.getPassedTests(), LogStatus.PASS, extent, basePath);
                buildTestNodes(context.getFailedTests(), LogStatus.FAIL, extent, basePath);
                buildTestNodes(context.getSkippedTests(), LogStatus.SKIP, extent, basePath);
            }
        }

        extent.flush();
        extent.close();
    }

    @SuppressWarnings({"unchecked", "unused"})
    private void buildTestNodes(IResultMap tests, LogStatus status, ExtentReports extent, String basePath) {
        AutomationCoreExtentTest test;
        String baseImagesPath = basePath + "images" + File.separator;

        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                if (shouldBeIncluded(result)) {
                    test = (AutomationCoreExtentTest) extent.startTest(AutomationCoreReportUtils.getTestCaseFullName(result));

                    test.setStartedTime(getTime(result.getStartMillis()));
                    test.setEndedTime(getTime(result.getEndMillis()));

                    for (String feature : AutomationCoreReportUtils.getFeatures(result)) {
                        test.assignCategory(feature);
                    }

                    if (AutomationCoreConfigWrapper.INSTANCE.getBoolean("extent.enable.browser.category")) {
                        Browser browser = AutomationCoreUtils.getBrowser(result);
                        if (browser != null) {
                            test.assignCategory("Browser: " + browser.toString());
                        }
                    }

                    List<Timestampable> logEvents = (List<Timestampable>) result.getAttribute("allEvents");

                    if (logEvents != null) {
                        for (Timestampable event : logEvents) {
                            if (event instanceof AutomationCoreLogRecord) {
                                AutomationCoreLogRecord record = (AutomationCoreLogRecord) event;
                                test.log(statusFromLevel(record.getLevel()),
                                        record.getMessage() +
                                                (record.getThrowable() != null ?
                                                        "<pre>" + ExceptionUtils.getFullStackTrace(record.getThrowable()) +
                                                                "</pre>" : ""),
                                        record.getTimestamp());
                            } else if (event instanceof Screenshot) {
                                Screenshot screenshot = (Screenshot) event;
                                try {
                                    File target = new File(baseImagesPath);
                                    FileUtils.copyFileToDirectory(screenshot.getFile(), target, true);
                                    test.log(LogStatus.INFO,
                                            screenshot.getName() + "<br/>" +
                                                    test.addScreenCapture("images/" + screenshot.getFile().getName()),
                                            screenshot.getTimestamp());
                                } catch (IOException ioe) {
                                    System.err.println(ExceptionUtils.getFullStackTrace(ioe));
                                }
                            }
                        }
                    }

                    Throwable throwable = result.getThrowable();

                    if (throwable != null) {
                        test.log(status, throwable, result.getEndMillis());
                    } else {
                        test.log(status, "Test " + status.toString().toLowerCase() + "ed", result.getEndMillis());
                    }

                    extent.endTest(test);
                }
            }
        }
    }

    protected boolean shouldBeIncluded(ITestResult result) {
        return (AutomationCoreUtils.getClassAnnotation(result.getInstance(), SkipExtentReport.class) == null) &&
                (AutomationCoreUtils.getMethodAnnotation(AutomationCoreReportUtils.getMethod(result), SkipExtentReport.class) == null);
    }

    protected LogStatus statusFromLevel(Level level) {
        switch (level) {
            case WARN:
                return LogStatus.WARNING;

            case ERROR:
                return LogStatus.ERROR;

            default:
                return LogStatus.INFO;
        }
    }

    protected Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    private String createReportDirectory(String path)    {
        File newDirectory = new File(path + "images");
        if (!newDirectory.mkdirs()) {
            System.out.println("Failed creating directory: " + path);
        }
        return path;
    }
}
