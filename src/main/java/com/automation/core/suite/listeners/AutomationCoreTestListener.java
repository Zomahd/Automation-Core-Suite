package com.automation.core.suite.listeners;

import com.automation.core.suite.configuration.AutomationCoreConfigWrapper;
import com.automation.core.suite.utils.AutomationCoreErrorUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.ITestResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AutomationCoreTestListener extends IAutomationCoreListener implements ITestListener {

    protected List<ITestListener> listeners = null;

    public AutomationCoreTestListener() {
        if (listeners == null) {
            listeners = new ArrayList<>();
            addListeners(AutomationCoreConfigWrapper.INSTANCE.get("automation.listeners", "com.automation.core.suite.listeners.AutomationCoreResultScreenshotListener," +
                    "com.automation.core.suite.listeners.AutomationCoreReportAttachmentsListener," +
                    "com.automation.core.suite.listeners.SauceLabsTestListener"));
        }
    }

    public void onTestStart(ITestResult result) {
        for (ITestListener listener : listeners) {
            try {
                listener.onTestStart(result);
            } catch (RuntimeException rte) {
                methodError("onTestStart", listener, rte);
            }
        }
    }

    public void onTestSuccess(ITestResult result) {
        for (ITestListener listener : listeners) {
            try {
                listener.onTestSuccess(result);
            } catch (RuntimeException rte) {
                methodError("onTestSuccess", listener, rte);
            }
        }
    }

    public void onTestFailure(ITestResult result) {
        for (ITestListener listener : listeners) {
            try {
                listener.onTestFailure(result);
            } catch (RuntimeException rte) {
                methodError("onTestFailure", listener, rte);
            }
        }
    }

    public void onTestSkipped(ITestResult result) {
        for (ITestListener listener : listeners) {
            try {
                listener.onTestSkipped(result);
            } catch (RuntimeException rte) {
                methodError("onTestSkipped", listener, rte);
            }
        }
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        for (ITestListener listener : listeners) {
            try {
                listener.onTestFailedButWithinSuccessPercentage(result);
            } catch (RuntimeException rte) {
                methodError("onTestFailedButWithinSuccessPercentage", listener, rte);
            }
        }
    }

    public void onStart(ITestContext context) {
        for (ITestListener listener : listeners) {
            try {
                listener.onStart(context);
            } catch (RuntimeException rte) {
                methodError("onStart", listener, rte);
            }
        }
    }

    public void onFinish(ITestContext context) {
        for (ITestListener listener : listeners) {
            try {
                listener.onFinish(context);
            } catch (RuntimeException rte) {
                methodError("onFinish", listener, rte);
            }
        }
    }


    protected void addListeners(String config) {
        String[] classes = config.split(",");
        for (String name : classes) {
            if (!"".equals(name.trim())) {
                try {
                    Class<?> clazz = Class.forName(name.trim());
                    Constructor<?> constructor = clazz.getConstructor();
                    Object instance = constructor.newInstance();

                    if (instance instanceof ITestListener) {
                        listeners.add((ITestListener) instance);
                    } else {
                        instanceError(CLASS_MUST_IMPLEMENT + " ITestListener", name);
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

    protected void instanceError(String msg, String name) {
        instanceError(msg, name, null);
    }

    protected void instanceError(String msg, String name, Throwable throwable) {
        AutomationCoreErrorUtils.log("AutomationCoreTestListener: " + INVALID_LISTENER + "\"" + name.trim() + "\", " + msg, throwable);
    }

    protected void methodError(String method, ITestNGListener listener, Throwable throwable) {
        AutomationCoreErrorUtils.log("AutomationCoreTestListener: " + ERROR_WHILE_EXEC + " \"" +
                method + "\" of \"" + ((ITestListener) listener).getClass().getName() + "\"", throwable);
    }
}
