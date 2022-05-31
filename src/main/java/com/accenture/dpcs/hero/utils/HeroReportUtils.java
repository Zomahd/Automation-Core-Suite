package com.accenture.dpcs.hero.utils;

import com.accenture.dpcs.hero.HeroTest;
import com.accenture.dpcs.hero.annotations.Title;
import com.accenture.dpcs.hero.configuration.HeroConfigWrapper;
import com.accenture.dpcs.hero.configuration.HeroPropertiesConfig;
import com.accenture.dpcs.hero.models.Browser;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HeroReportUtils {


    public static Method getMethod(ITestResult result) {
        return result.getMethod().getConstructorOrMethod().getMethod();
    }

    public static String getMethodTitle(ITestResult result, boolean includeParameters, boolean excludeBrowser) {
        String title = result.getMethod().getMethodName();
        Method method = getMethod(result);
        Title methodTitle = HeroUtils.getMethodAnnotation(method, Title.class);

        if (methodTitle != null) {
            title = methodTitle.value();
        }

        if (includeParameters) {
            String parameters = getMethodParametersAsString(result, excludeBrowser);
            if (parameters.length() > 0) {
                title += " [" + parameters + "]";
            }
        }

        return title;
    }

    public static String getMethodTitle(Method method, Object[] parameters, boolean includeParameters, boolean excludeBrowser) {
        String title = method.getName();
        Title methodTitle = HeroUtils.getMethodAnnotation(method, Title.class);

        if (methodTitle != null) {
            title = methodTitle.value();
        }

        if (includeParameters) {
            String parametersStr = getMethodParametersAsString(parameters, excludeBrowser);
            if (parametersStr.length() > 0) {
                title += " [" + parametersStr + "]";
            }
        }

        return title;
    }

    public static String getClassTitle(ITestResult result) {
        Title title = HeroUtils.getClassAnnotation(result.getInstance(), Title.class);

        if (title != null) {
            return title.value();
        }

        String fullClassName = result.getTestClass().getName();
        return fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
    }

    public static String getClassTitle(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        Title title = HeroUtils.getClassAnnotation(clazz, Title.class);

        if (title != null) {
            return title.value();
        }

        String fullClassName = clazz.getName();
        return fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
    }

    public static String getMethodParametersAsString(ITestResult result, boolean excludeBrowser) {
        return getMethodParametersAsString(result.getParameters(), excludeBrowser);
    }

    public static String getMethodParametersAsString(Object[] parameters, boolean excludeBrowser) {
        StringBuilder builder = new StringBuilder();

        for (Object param : parameters) {
            if (builder.length() > 0) {
                builder.append(", ");
            }

            if (param != null) {
                if (!(param instanceof Browser) || !excludeBrowser) {
                    builder.append(param.toString());
                }
            } else {
                builder.append("null");
            }
        }

        return builder.toString();
    }

    public static String getBrowser(ITestResult result) {
        Browser browser = HeroUtils.getBrowser(result);
        return browser != null ? browser.toString() : "";
    }

    public static String getTestCaseFullName(ITestResult result) {
        return getClassTitle(result) + ":" + getMethodTitle(result, true, false);
    }

    public static String getTestCaseFullName(Method method, Object[] parameters) {
        return getClassTitle(method) + ":" + getMethodTitle(method, parameters, true, false);
    }

    public static String getTestCaseFullNameNoBrowser(ITestResult result) {
        return getClassTitle(result) + ":" + getMethodTitle(result, true, true);
    }

    public static String getTestCaseFullNameNoBrowser(Method method, Object[] parameters) {
        return getClassTitle(method) + ":" + getMethodTitle(method, parameters, true, true);
    }

    public static String getExecutionTagName() {
        return HeroConfigWrapper.INSTANCE.get("saucelabs.listener.tag.prefix", "Hero") + " " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getExecutionTagName(ITestResult result) {
        Object name = result.getTestContext().getAttribute("executionName");
        return name != null ? name.toString() : null;
    }

    public static String[] getFeatures(ITestResult result) {
        return result.getMethod().getGroups();
    }

    public static String getWebDriverSessionId(ITestResult result) {
        String sessionId = null;

        if (result.getInstance() instanceof HeroTest) {
            sessionId = ((HeroTest) result.getInstance()).getWebDriverSessionId();
        }

        return sessionId != null ? sessionId : "";
    }
}
