package com.accenture.dpcs.hero.utils;

import com.accenture.dpcs.hero.models.Browser;
import com.accenture.dpcs.hero.models.Timestampable;
import com.accenture.dpcs.hero.webdriver.SupportedWebDriverService;
import org.testng.ITestResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class HeroUtils {

    public static <A extends Annotation> A getClassAnnotation(Object instance, Class<A> annotationClass) {
        if (instance != null) {
            return instance.getClass().getAnnotation(annotationClass);
        } else {
            return null;
        }
    }

    public static <A extends Annotation> A getClassAnnotation(Class<?> clazz, Class<A> annotationClass) {
        return clazz.getAnnotation(annotationClass);
    }

    public static <A extends Annotation> A getMethodAnnotation(Method method, Class<A> annotationClass) {
        if (method != null) {
            return method.getDeclaredAnnotation(annotationClass);
        } else {
            return null;
        }
    }

    public static boolean isClassPresent(Object[] arr, Class clazz) {
        if (arr != null) {
            for (Object obj : arr) {
                if (clazz.isInstance(obj)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Object[][] listToObjectArray(List list) {
        Object[][] data = null;

        if (list != null) {
            data = new Object[list.size()][];

            for (int i = 0; i < list.size(); i++) {
                data[i] = new Object[] { list.get(i) };
            }
        }

        return data;
    }

    public static List<Timestampable> sort(List<Timestampable> list) {
        Collections.sort(list, new Comparator<Timestampable>() {
            public int compare(Timestampable o1, Timestampable o2) {
                return ((Long) o1.getTimestamp()).compareTo(o2.getTimestamp());
            }
        });

        return list;
    }

    public static Browser getBrowser(ITestResult result) {
        Object browserAttr = result.getAttribute("browser");

        if (browserAttr != null && browserAttr instanceof Browser) {
            return (Browser) browserAttr;
        }

        for (Object param : result.getParameters()) {
            if (param instanceof Browser) {
                return (Browser) param;
            }
        }

        return null;
    }

    public static boolean hasBrowserService(ITestResult result, SupportedWebDriverService service) {
        Browser browser = getBrowser(result);
        return browser != null && service.getId().equals(browser.getService());
    }
}
