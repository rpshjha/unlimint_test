package com.unlimint.core;

import lombok.extern.log4j.Log4j;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

import static com.unlimint.core.ChromeDriverInstance.createDriverUsingChrome;
import static com.unlimint.core.FirefoxDriverInstance.createDriverUsingFirefox;
import static com.unlimint.core.SafariDriverInstance.createDriverUsingSafari;
import static com.unlimint.utils.PropertyReader.get;

@Log4j
public class DriverInstance {

    private static final ThreadLocal<WebDriver> wDriver = new ThreadLocal<>();

    private DriverInstance() {
    }

    /**
     * Gets the browser driver.
     *
     * @param browserName the browser
     * @return the browser driver
     */
    private static WebDriver getBrowserDriver(String browserName) {
        WebDriver driver = null;

        if (browserName == null)
            browserName = "chrome";

        switch (browserName) {
            case "firefox":
                driver = createDriverUsingFirefox();
                break;
            case "chrome":
                driver = createDriverUsingChrome();
                break;
            case "safari":
                driver = createDriverUsingSafari();
                break;
            default:
                break;
        }

        assert driver != null;
        if (Boolean.parseBoolean(get("isWindowMax")))
            driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Long.parseLong(get("page.load.timeout"))));

        return driver;
    }

    public static void initializeDriver(final String browser) {
        log.info("initializing the web-driver");
        wDriver.set(getBrowserDriver(browser));
    }

    /**
     * @return WebDriver
     */
    public static WebDriver getDriver() {
        if (wDriver.get() == null)
            throw new RuntimeException("webDriver not initialized.please initialize the driver by calling initializeDriver(final String browser) method");

        return wDriver.get();
    }


    /**
     * kills the driver
     */
    public static void killDriver() {
        if (getDriver() != null) {
            log.info("closing the browser");
            getDriver().quit();
        }
        wDriver.remove();
    }
}
