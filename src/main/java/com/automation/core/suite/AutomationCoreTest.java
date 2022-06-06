package com.automation.core.suite;

import com.automation.core.suite.configuration.BrowserFactory;
import com.automation.core.suite.configuration.SelenideConfig;
import com.automation.core.suite.exceptions.UnknownBrowserException;
import com.automation.core.suite.models.Browser;
import com.automation.core.suite.models.Screenshot;
import com.automation.core.suite.models.Timestampable;
import com.automation.core.suite.utils.AutomationCoreReportUtils;
import com.automation.core.suite.utils.AutomationCoreUtils;
import com.automation.core.suite.webdriver.WebDriverFactory;
import com.codeborne.selenide.Screenshots;
import com.codeborne.selenide.WebDriverRunner;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AutomationCoreTest extends AutomationCoreBase {

    protected ThreadLocal<Boolean> webdriverInitialized = new ThreadLocal<Boolean>();
    protected ThreadLocal<String> webDriverSessionId = new ThreadLocal<String>();
    protected ThreadLocal<List<Screenshot>> screenshots = new ThreadLocal<List<Screenshot>>();
    protected Browser browser = null;

    public AutomationCoreTest() {
    }

    public AutomationCoreTest(Browser browser) {
        this.browser = browser;
    }

    @DataProvider(name = "browsers", parallel = true)
    protected static Object[][] getAllBrowsers(ITestContext context) {
        loadBrowsers(context);
        return AutomationCoreUtils.listToObjectArray(BrowserFactory.getBrowsers());
    }

    @DataProvider(name = "browsersDesktop", parallel = true)
    protected static Object[][] getDesktopBrowsers(ITestContext context) {
        loadBrowsers(context);
        return AutomationCoreUtils.listToObjectArray(BrowserFactory.getDesktopBrowsers());
    }

    @DataProvider(name = "mobileDesktop", parallel = true)
    protected static Object[][] getMobileBrowsers(ITestContext context) {
        loadBrowsers(context);
        return AutomationCoreUtils.listToObjectArray(BrowserFactory.getMobileBrowsers());
    }

    protected static void loadBrowsers(ITestContext context) {
        String browserConfig = context.getCurrentXmlTest().getParameter("browsers");

        if (browserConfig == null || browserConfig.length() == 0) {
            browserConfig = "default";
        }

        BrowserFactory.load(browserConfig);
    }

    @BeforeMethod(alwaysRun = true)
    protected void beforeAutomationCoreTestMethod(Object[] args) {
        SelenideConfig.set();
        webdriverInitialized.set(false);
        screenshots.set(new ArrayList<Screenshot>());

        // Initialize the browser if found in the parameters
        if (browser != null) {
            initWebDriver(browser);
        } else if (args != null && args.length > 0) {
            for (Object arg : args) {
                if (arg instanceof Browser) {
                    initWebDriver((Browser) arg);
                    break;
                }
            }
        }
    }

    @AfterMethod(alwaysRun = true)
    protected void afterAutomationCoreTestMethod() {
        SelenideConfig.set();
        webdriverInitialized.set(false);
        WebDriverRunner.closeWebDriver();
    }

    public void screenshot(String name) {
        if (isWebdriverInitialized()) {
            File file = Screenshots.takeScreenShotAsFile();
            if (file != null) {
                screenshots.get().add(new Screenshot(name, file));
            }
        }
    }

    protected WebDriver initWebDriver(Browser browser) {
        WebDriver wd = null;

        if (webdriverInitialized.get()) {
            wd = getWebDriver();
        } else {
            try {
                wd = WebDriverFactory.get(browser);
                WebDriverRunner.setWebDriver(wd);
                webdriverInitialized.set(true);
            } catch (UnknownBrowserException ube) {
                System.err.println(ube.getMessage());
            }
        }

        setWebDriverSessionId(wd);

        return wd;
    }

    protected WebDriver getWebDriver() {
        return WebDriverRunner.getWebDriver();
    }

    public List<Screenshot> getScreenshots() {
        return screenshots.get();
    }

    public List<Timestampable> getAllEvents() {
        List<Timestampable> events = super.getAllEvents();
        events.addAll(screenshots.get());
        return AutomationCoreUtils.sort(events);
    }

    public boolean isWebdriverInitialized() {
        return webdriverInitialized.get();
    }

    public String getWebDriverSessionId() {
        return webDriverSessionId.get();
    }

    public void setWebDriverSessionId(String sessionId) {
        webDriverSessionId.set(sessionId);
    }

    public void setWebDriverSessionId(WebDriver wd) {
        if (wd != null && wd instanceof RemoteWebDriver) {
            setWebDriverSessionId(((RemoteWebDriver) wd).getSessionId().toString());
        }
    }

    public Browser getBrowser() {
        return browser;
    }

    @Override
    protected void initialExecutionLog(Method method, Object[] parameters) {

        if (browser != null) {
            ArrayUtils.reverse(parameters);
            parameters = ArrayUtils.add(parameters, browser);
            ArrayUtils.reverse(parameters);
        }

        getLog().info("Executing " + AutomationCoreReportUtils.getTestCaseFullName(method, parameters) + "...");
    }
}
