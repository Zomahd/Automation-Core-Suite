package com.automation.core.suite.webdriver;

import com.automation.core.suite.configuration.AutomationCoreConfigWrapper;
import com.automation.core.suite.exceptions.UnknownBrowserException;
import com.automation.core.suite.models.Browser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class WebDriverBuilder {

    public static WebDriver local(Browser browser, DesiredCapabilities capabilities) throws UnknownBrowserException {
        String browserName = browser.getBrowser();
        WebDriver wd = null;

        if ("chrome".equals(browserName)) {
            System.setProperty("webdriver.chrome.driver", AutomationCoreConfigWrapper.INSTANCE.get("wd.chrome.driver", ""));

            ChromeOptions options = new ChromeOptions();
            options.merge(capabilities);

            //Using custom chrome profile
            String chromeProfilePath = AutomationCoreConfigWrapper.INSTANCE.get("chrome.profile.destination", "");
            if (chromeProfilePath != null && !chromeProfilePath.isEmpty()) {
                options.addArguments("chrome.switches", "--disable-extensions");
                options.addArguments("user-data-dir=" + chromeProfilePath);
            }

            String arg = AutomationCoreConfigWrapper.INSTANCE.get("driver.chrome.arguments", "");
            String binary = AutomationCoreConfigWrapper.INSTANCE.get("driver.chrome.binary.location", "");

            addArgumentsToChromeOptions(options, arg);
            addBinaryToChromeOptions(options, binary);

            wd = new ChromeDriver(options);

        } else if ("edge".equals(browserName)) {
            wd = new EdgeDriver(capabilities);
        } else if ("internetExplorer".equals(browserName)) {
            wd = new InternetExplorerDriver(capabilities);
        } else if ("firefox".equals(browserName)) {
            System.setProperty("webdriver.gecko.driver", AutomationCoreConfigWrapper.INSTANCE.get("wd.firefox.driver", ""));
            wd = new FirefoxDriver(capabilities);
        }   else    {
            throw new UnknownBrowserException("Unknown browser: " + browserName);
        }

        return wd;
    }



    public static WebDriver remote(Browser browser, DesiredCapabilities capabilities) {

        String remoteUrl = browser.getRemoteUrl();

        if (remoteUrl == null) {
            remoteUrl = AutomationCoreConfigWrapper.INSTANCE.get("wd.remote.url", "");
            
        	if (null != browser.getPort()) {
        		remoteUrl = remoteUrl.replace("4444", browser.getPort());
        	}
        	
        }

        try {
            return new RemoteWebDriver(new URL(remoteUrl), capabilities);
        } catch (MalformedURLException mre) {
            throw new RuntimeException("Malformed Remote Web Driver URL", mre);
        }
    }


    @SuppressWarnings("unused")
    public static WebDriver saucelabs(Browser browser, DesiredCapabilities capabilities) {

        try {
            return new RemoteWebDriver(new URL(getSauceLabsURL()), capabilities);
        } catch (MalformedURLException mre) {
            throw new RuntimeException("Malformed Sauce Labs URL", mre);
        }
    }



    protected static String getSauceLabsURL() {
        String url = AutomationCoreConfigWrapper.INSTANCE.get("saucelabs.url");

        if (url == null) {
            url = "http://{username}:{accessKey}@ondemand.saucelabs.com:80/wd/hub";
        }

        url = url.replace("{username}", AutomationCoreConfigWrapper.INSTANCE.get("saucelabs.username", ""));
        url = url.replace("{accessKey}", AutomationCoreConfigWrapper.INSTANCE.get("saucelabs.accessKey", ""));

        return url;
    }

    protected static void addArgumentsToChromeOptions(ChromeOptions options, String arguments) {

        List<String> listOfArguments = null;

        if(!arguments.isEmpty() && !arguments.equalsIgnoreCase("")) {
            listOfArguments = Arrays.asList(arguments.split(","));
        }

        if(listOfArguments != null && !listOfArguments.isEmpty()) {
            options.addArguments(listOfArguments);
        }

    }

    protected static void addBinaryToChromeOptions(ChromeOptions options, String binaryPath) {
        if(binaryPath != null && !binaryPath.isEmpty()) {
            options.setBinary(binaryPath);
        }
    }
}
