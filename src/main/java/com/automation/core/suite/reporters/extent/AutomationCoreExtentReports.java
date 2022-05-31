package com.automation.core.suite.reporters.extent;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;

import java.util.ArrayList;
import java.util.Locale;

@SuppressWarnings("unused")
public class AutomationCoreExtentReports extends ExtentReports {

    public AutomationCoreExtentReports(String filePath, Boolean replaceExisting, DisplayOrder displayOrder, NetworkMode networkMode, Locale locale) {
        super(filePath, replaceExisting, displayOrder, networkMode, locale);
    }

    public AutomationCoreExtentReports(String filePath, Boolean replaceExisting, DisplayOrder displayOrder, NetworkMode networkMode) {
        super(filePath, replaceExisting, displayOrder, networkMode);
    }

    public AutomationCoreExtentReports(String filePath, Boolean replaceExisting, DisplayOrder displayOrder, Locale locale) {
        super(filePath, replaceExisting, displayOrder, locale);
    }

    public AutomationCoreExtentReports(String filePath, Boolean replaceExisting, DisplayOrder displayOrder) {
        super(filePath, replaceExisting, displayOrder);
    }

    public AutomationCoreExtentReports(String filePath, Boolean replaceExisting, NetworkMode networkMode, Locale locale) {
        super(filePath, replaceExisting, networkMode, locale);
    }

    public AutomationCoreExtentReports(String filePath, Boolean replaceExisting, NetworkMode networkMode) {
        super(filePath, replaceExisting, networkMode);
    }

    public AutomationCoreExtentReports(String filePath, NetworkMode networkMode) {
        super(filePath, networkMode);
    }

    public AutomationCoreExtentReports(String filePath, Boolean replaceExisting, Locale locale) {
        super(filePath, replaceExisting, locale);
    }

    public AutomationCoreExtentReports(String filePath, Boolean replaceExisting) {
        super(filePath, replaceExisting);
    }

    public AutomationCoreExtentReports(String filePath, Locale locale) {
        super(filePath, locale);
    }

    public AutomationCoreExtentReports(String filePath) {
        super(filePath);
    }

    @Override
    public synchronized ExtentTest startTest(String testName, String description) {
        if(this.testList == null) {
            this.testList = new ArrayList<ExtentTest>();
        }

        ExtentTest test = new AutomationCoreExtentTest(testName, description);
        this.updateTestQueue(test);
        return test;
    }
}
