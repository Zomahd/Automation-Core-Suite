package com.automation.core.suite.listeners;

import com.automation.core.suite.configuration.AutomationCoreConfigWrapper;
import com.automation.core.suite.utils.AutomationCoreErrorUtils;
import org.apache.commons.io.FileUtils;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AutomationCoreReportListener extends IAutomationCoreListener implements IReporter {

    public final static String REPORT_ARTIFACT_DIRECTORY = "artifacts";
    public final static String REPORT_LATEST_DIRECTORY = "latest";
    protected List<IReporter> listeners = null;
    private boolean generated = false;

    public AutomationCoreReportListener() {
        if(listeners == null) {
            listeners = new ArrayList<>();
            addListeners(AutomationCoreConfigWrapper.INSTANCE.get("automation.reporters",
                    "com.automation.core.suite.reporters.excel.ExcelReporter," +
                    "com.automation.core.suite.reporters.extent.ExtentReporter"));
        }
    }

    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        if (!generated) {
            generated = true;

            //String basePath = outputDirectory + File.separator + ".." + File.separator + REPORT_ARTIFACT_DIRECTORY + File.separator;
            String basePath = outputDirectory + File.separator + REPORT_ARTIFACT_DIRECTORY + File.separator;

            String dateOutputDirectory =basePath + getReportDateFolder() + File.separator;
            String latestOutputDirectory =basePath + REPORT_LATEST_DIRECTORY + File.separator;

            if (!createDirectory(dateOutputDirectory)) {
                System.out.println("Failed creating directory: " + dateOutputDirectory);
            }

            for (IReporter reporter : listeners) {
                reporter.generateReport(xmlSuites, suites, dateOutputDirectory);
            }

            if (!deleteDirectory(latestOutputDirectory)) {
                System.out.println("Failed deleting directory: " + latestOutputDirectory);
            }

            if(includeLatestDirectory()) {
                if (!copyDirectory(dateOutputDirectory, latestOutputDirectory)) {
                    System.out.println("Failed creating directory: " + latestOutputDirectory);
                }
            }
        }
    }

    private String getReportDateFolder()  {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH.mm.ss.SS");
        return sdf.format(calendar.getTime());
    }

    private boolean createDirectory(String path) {
        File newDirectory = new File(path);
        return newDirectory.mkdirs();
    }

    private boolean deleteDirectory(String path) {
        File directory = new File(path);
        return directory.delete();
    }

    private boolean copyDirectory(String src, String dst) {
        try {
            FileUtils.copyDirectory(new File(src), new File(dst), true);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    private boolean includeLatestDirectory() {
        return AutomationCoreConfigWrapper.INSTANCE.getBoolean("automation.reports.include.latest.directory");
    }

    @Override
    protected void addListeners(String config) {
        String[] classes = config.split(",");
        for (String name : classes) {
            if (!"".equals(name.trim())) {
                try {
                    Class<?> clazz = Class.forName(name.trim());
                    Constructor<?> constructor = clazz.getConstructor();
                    Object instance = constructor.newInstance();

                    if (instance instanceof IReporter) {
                        listeners.add((IReporter) instance);
                    } else {
                        instanceError(CLASS_MUST_IMPLEMENT + " IReporter", name);
                    }

                } catch (ClassNotFoundException cnfe) {
                    instanceError(CLASS_NOT_FOUND, name);
                } catch (NoSuchMethodException nsme) {
                    instanceError(CLASS_NO_PARAM_CONSTRUCTOR, name);
                } catch (IllegalAccessException iae) {
                    instanceError(CLASS_CANNOT_BE_ACCESSED, name);
                } catch (InstantiationException ie) {
                    instanceError(ERROR_INST_CLASS, name, ie);
                } catch (InvocationTargetException ite) {
                    instanceError(INVOCATION_TARGET_EXCEPTION, name, ite);
                }
            }
        }
    }

    @Override
    protected void instanceError(String msg, String name) {
        instanceError(msg, name, null);
    }

    @Override
    protected void instanceError(String msg, String name, Throwable throwable) {
        AutomationCoreErrorUtils.log("AutomationCoreReportListener: " + INVALID_LISTENER + "\"" + name.trim() + "\", " + msg, throwable);
    }

    @Override
    protected void methodError(String method, ITestNGListener listener, Throwable throwable) {
        AutomationCoreErrorUtils.log("AutomationCoreReportListener: " + ERROR_WHILE_EXEC + " \"" +
                method + "\" of \"" + ((ITestListener)listener).getClass().getName() + "\"", throwable);
    }

}
