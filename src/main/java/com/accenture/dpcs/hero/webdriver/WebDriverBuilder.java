package com.accenture.dpcs.hero.webdriver;

import com.accenture.dpcs.hero.HeroSessionPoolTest;
import com.accenture.dpcs.hero.configuration.HeroConfigWrapper;
import com.accenture.dpcs.hero.exceptions.UnknownBrowserException;
import com.accenture.dpcs.hero.models.Browser;
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
            System.setProperty("webdriver.chrome.driver", HeroConfigWrapper.INSTANCE.get("wd.chrome.driver", ""));

            ChromeOptions options = new ChromeOptions();
            options.merge(capabilities);

            //Using custom chrome profile
            String chromeProfilePath = HeroConfigWrapper.INSTANCE.get("chrome.profile.destination", "");
            if (chromeProfilePath != null && !chromeProfilePath.isEmpty()) {
                options.addArguments("chrome.switches", "--disable-extensions");
                options.addArguments("user-data-dir=" + chromeProfilePath);
            }

            String arg = HeroConfigWrapper.INSTANCE.get("driver.chrome.arguments", "");
            String binary = HeroConfigWrapper.INSTANCE.get("driver.chrome.binary.location", "");

            addArgumentsToChromeOptions(options, arg);
            addBinaryToChromeOptions(options, binary);

            wd = new ChromeDriver(options);

        } else if ("edge".equals(browserName)) {
            wd = new EdgeDriver(capabilities);
        } else if ("internetExplorer".equals(browserName)) {
            wd = new InternetExplorerDriver(capabilities);
        } else if ("firefox".equals(browserName)) {
            System.setProperty("webdriver.gecko.driver", HeroConfigWrapper.INSTANCE.get("wd.firefox.driver", ""));
            wd = new FirefoxDriver(capabilities);
        }   else    {
            throw new UnknownBrowserException("Unknown browser: " + browserName);
        }

        return wd;
    }



    public static WebDriver remote(Browser browser, DesiredCapabilities capabilities) {

        String remoteUrl = browser.getRemoteUrl();

        if (remoteUrl == null) {
            remoteUrl = HeroConfigWrapper.INSTANCE.get("wd.remote.url", "");
            
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
        String url = HeroConfigWrapper.INSTANCE.get("saucelabs.url");

        if (url == null) {
            url = "http://{username}:{accessKey}@ondemand.saucelabs.com:80/wd/hub";
        }

        url = url.replace("{username}", HeroConfigWrapper.INSTANCE.get("saucelabs.username", ""));
        url = url.replace("{accessKey}", HeroConfigWrapper.INSTANCE.get("saucelabs.accessKey", ""));

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
