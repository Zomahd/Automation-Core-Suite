package com.automation.core.suite;

import com.automation.core.suite.listeners.AutomationCoreReportListener;
import com.automation.core.suite.listeners.AutomationCoreTestListener;
import com.automation.core.suite.logs.AutomationCoreLogger;
import com.automation.core.suite.models.Timestampable;
import com.automation.core.suite.utils.AutomationCoreReportUtils;
import com.automation.core.suite.utils.AutomationCoreUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Listeners({AutomationCoreTestListener.class, AutomationCoreReportListener.class})
public class AutomationCoreBase {
    protected ThreadLocal<AutomationCoreLogger> log = new ThreadLocal<AutomationCoreLogger>();

    @BeforeMethod(alwaysRun = true)
    protected void beforeAutomationCoreBaseMethod(Method method, Object[] parameters) {
        log.set(new AutomationCoreLogger());
        initialExecutionLog(method, parameters);
    }

    @AfterMethod(alwaysRun = true)
    protected void afterAutomationCoreBaseMethod() {
        log.get().flush();
    }

    public AutomationCoreLogger getLog() {
        return log.get();
    }

    public List<Timestampable> getAllEvents() {
        List<? extends Timestampable> logRecords = getLog().getRecords();
        List<Timestampable> events = new ArrayList<Timestampable>(logRecords.size());
        events.addAll(logRecords);
        return AutomationCoreUtils.sort(events);
    }

    protected void initialExecutionLog(Method method, Object[] parameters) {
        getLog().info("Executing " + AutomationCoreReportUtils.getTestCaseFullName(method, parameters) + "...");
    }
}
