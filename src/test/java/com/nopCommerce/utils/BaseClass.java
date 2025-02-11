package com.nopCommerce.utils;


import com.nopCommerce.factory.Loader;
import com.nopCommerce.factory.ReadFiles;
import com.nopCommerce.factory.Reports;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

@Listeners({com.nopCommerce.factory.Reports.class, com.nopCommerce.factory.AnnotationTransformer.class})
public class BaseClass {

    private final Logger logger = LogManager.getLogger("BaseClass.class");
    public final Loader loader = Loader.getLoader();

    public ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @Parameters({"Region"})
    @BeforeSuite
    public void beforeSuite(@Optional() String regionParam){

        logger.info(Reports.setAutomationSuiteLog());
        logger.info("Automation suite is started on {} environment!!", regionParam);

        logger.info("**** Entered before suite ****");

        ReadFiles.loadConfigFiles();
        logger.info("All config files are loaded successfully");

        loader.setRegion( regionParam!=null ? regionParam : ReadFiles.config.getProperty("Region"));
        if(loader.getRegion().equals("QA")){

            loader.setUrl(ReadFiles.config.getProperty("QA_Url"));
            loader.setUserId(ReadFiles.config.getProperty("QA_UserId"));
            loader.setPassword(ReadFiles.config.getProperty("QA_Password"));
            loader.setUserName(ReadFiles.config.getProperty("QA_UserName"));
            loader.setCurrency(ReadFiles.config.getProperty("QA_Currency"));
        } else if (loader.getRegion().equals("UAT")) {

            loader.setUrl(ReadFiles.config.getProperty("UAT_Url"));
            loader.setUserId(ReadFiles.config.getProperty("UAT_UserId"));
            loader.setPassword(ReadFiles.config.getProperty("UAT_Password"));
            loader.setUserName(ReadFiles.config.getProperty("UAT_UserName"));
            loader.setCurrency(ReadFiles.config.getProperty("UAT_Currency"));
        }
        else Assert.fail("Unknown region is found....So, Suite got aborted!!!!");

        logger.info("All {} region details are set!!", loader.getRegion());
    }

    @Parameters({"Browser"})
    @BeforeTest
    public void beforeTests(@Optional() String browserParam){

        logger.info("*** Entered before tests ***");
        browserParam = browserParam!=null ? browserParam : ReadFiles.config.getProperty("Browser");

        if (browserParam.equals("Chrome")) {
            loader.setChromeOptions("--start-maximized");
        } else if (browserParam.equals("Edge")) {
            loader.setEdgeOptions("--start-maximized");
            loader.setEdgeOptions("--disable-extensions");
            loader.setEdgeOptions("--ignore-certificate-errors");
        } else if (browserParam.equals("Firefox")) {
            loader.setFirefoxOptions("--start-maximized");
        } else
            throw new IllegalArgumentException("Unknown browser type was found as "+browserParam+"...So, Tests got aborted!!");

        logger.info("All {} browser config's are set!!", browserParam);
    }

    @Parameters({"Browser"})
    @BeforeClass
    public void beforeClass(@Optional String browser){

        logger.info("** Entered before class **");
        loader.setBrowser(browser!=null ? browser : ReadFiles.config.getProperty("Browser"));

        if(loader.getBrowser().equals("Chrome")){
            driver.set(new ChromeDriver(loader.getChromeOptions()));
        }
        else if(loader.getBrowser().equals("Edge")){
            driver.set(new EdgeDriver(loader.getEdgeOptions()));
        }
        else if(loader.getBrowser().equals("Firefox")){
            driver.set(new FirefoxDriver(loader.getFirefoxOptions()));
        }
        else
            throw new IllegalArgumentException("Unknown browser was found as "+loader.getBrowser()+"..So, Test marked as failed!!");

        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        logger.info("{} browser was loaded successfully..", loader.getBrowser());
    }

    @AfterClass
    public void afterClass(){

        logger.info("** Entered after class **");
        if(driver.get() != null){
            driver.get().quit();
            driver.remove();
            logger.info("{} browser was closed..", loader.getBrowser());
            loader.removeBrowser();
        }
    }

    @AfterTest
    public void afterTests(){
        logger.info("*** Entered after tests ***");
    }

    @AfterSuite
    public void afterSuite(){
        logger.info("+---- Automation run got completed -----+");
    }

    public WebDriver getDriver(){
        return driver.get();
    }

}
