package com.nopCommerce.factory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reports implements ITestListener {

    private final static Logger logger = LogManager.getLogger("Reports.class");
    private ExtentReports extentReports;
    private ExtentTest test;
    private String timeStamp;
    private long duration = 0;

    @Override
    public void onStart(ITestContext context){

        timeStamp = new SimpleDateFormat("yyyyMMdd_HH.mm.ss").format(new Date());

        ExtentSparkReporter reports = new ExtentSparkReporter(System.getProperty("user.dir")+"//reports//Test_Report_"+timeStamp+"//index.html");
        reports.config().setDocumentTitle("Automation Report");
        reports.config().setReportName("DemoQA Test Report");
        reports.config().setTimelineEnabled(true);
        reports.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        extentReports = new ExtentReports();
        extentReports.attachReporter(reports);
        extentReports.setSystemInfo("Project Name", "Demo QA");
        extentReports.setSystemInfo("Automation Tester", "Vamsi Krishna");
        extentReports.setSystemInfo("OS Type", "Windows 10 Home");
        //logger.info(setAutomationSuiteLog());
        //logger.info("{} tests are started on {} environment!!", context.getCurrentXmlTest().getName(), context.getCurrentXmlTest().getParameter("Region"));
    }

    @Override
    public void onTestStart(ITestResult result){

        test = extentReports.createTest(result.getMethod().getDescription());
        logger.info(getTestStartedLog(result.getMethod().getDescription(), result.getTestClass().getName()+"/"+result.getMethod().getMethodName()));
    }

    @Override
    public void onTestSuccess(ITestResult result){

        duration = (result.getEndMillis()-result.getStartMillis())/1000;
        test.pass("Test Passed");
        getTestStatusLog(result.getMethod().getDescription(), "PASSED");
        logger.info("Test finished outcome :: Test case:{}/{}, Status:Passed, Duration:{}, Failure cause:null", result.getTestClass().getName(),result.getMethod().getMethodName(),  duration);
    }

    @Override
    public void onTestFailure(ITestResult result){

        String status;
        duration = (result.getEndMillis()-result.getStartMillis())/1000;

        if(!(result.getThrowable() instanceof AssertionError)){
            test.log(Status.FAIL,MarkupHelper.createLabel(result.getMethod().getMethodName(),ExtentColor.ORANGE));
            getTestStatusLog(result.getMethod().getDescription(), "ERROR");
            status = "Error";
        }
        else {
            test.fail(result.getThrowable());
            getTestStatusLog(result.getMethod().getDescription(), "FAILED");
            status = "Failed";
        }
        try{
            ThreadLocal<WebDriver> driver = (ThreadLocal<WebDriver>) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
            File source = ((TakesScreenshot)driver.get()).getScreenshotAs(OutputType.FILE);
            String targetPath = System.getProperty("user.dir")+"//reports//Test_Report_"+timeStamp+"//screenshots//"+result.getMethod().getMethodName()+".png";
            FileUtils.copyFile(source, new File(targetPath));
            test.addScreenCaptureFromPath(targetPath, result.getMethod().getMethodName());
            logger.info("{} test screenshot is attached to the report.", result.getMethod().getMethodName());
        } catch (Exception e) {
            logger.error("Screenshot was n't captured by below error: ", e);
            throw new RuntimeException(e);
        }
        logger.info("Test finished outcome :: Test case:{}/{}, Status:{}, Duration:{}, Failure cause:{}", result.getTestClass().getName(),result.getMethod().getMethodName(), status,  duration, result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result){

        test.skip("Test Skipped");
        logger.info("Test finished outcome :: Test case:{}/{}, Status:skipped, Duration:{}, Failure cause:{}", result.getTestClass().getName(),result.getMethod().getMethodName(),  duration, result.getSkipCausedBy().toString());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result){
        test.info(String.valueOf(result.getMethod().getSuccessPercentage()));
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result){
        this.onTestFailure(result);
    }

    @Override
    public void onFinish(ITestContext context){
        getRunResults(context);
        extentReports.flush();
    }

    public static void getRunResults(ITestContext context){
        int totalTests = context.getAllTestMethods().length;
        int passedTests = context.getPassedTests().size();
        int failedTests = context.getFailedTests().size();
        String suiteName = context.getSuite().getName();
        int skippedTests = context.getSkippedTests().size();
        String endDate = context.getEndDate().toString();
        String startDate = context.getStartDate().toString();
        String host = context.getHost() == null ? "Local" : context.getHost();

        // Table header
        String separator = "+-----------------+---------------------+";
        logger.info(separator);
        logger.info(String.format("| %-15s | %-19s |", "Execution Summary", suiteName));
        logger.info(separator);
        logger.info(String.format("| %-15s | %-19s |", "Total Tests", totalTests));
        logger.info(String.format("| %-15s | %-19s |", "Passed Tests", passedTests));
        logger.info(String.format("| %-15s | %-19s |", "Failed Tests", failedTests));
        logger.info(String.format("| %-15s | %-19s |", "Skipped Tests", skippedTests));
        logger.info(String.format("| %-15s | %-19s |", "Start Time", startDate));
        logger.info(String.format("| %-15s | %-19s |", "End Time", endDate));
        logger.info(String.format("| %-15s | %-19s |", "Host", host));
        logger.info(separator);
    }



    public static String setAutomationSuiteLog() {
        return
                "\n    _     __  __  _______   ___   ____   ____     _   _______  _   ___   ____    _                 \n" +
                        "   / \\   | |  | ||__   __| / _ \\ | |\\ \\ / /| |   / \\ |__   __|| | / - \\ | |\\ \\  | |    \n" +
                        "  / _ \\  | |  | |   | |   | / \\ || |  \\_/  | |  / _ \\   | |   | || / \\ || | \\ \\ | |     \n" +
                        " / ___ \\ | \\__/ |   | |   | \\_/ || |       | | / --- \\  | |   | || \\_/ || |  \\ \\| |     \n" +
                        "/_/   \\_\\ \\____/    |_|    \\___/ |_|       |_|/_/   \\_\\ |_|   |_| \\___/ |_|   \\__/     \n" +
                        "--------------------------------------------------------------------------------------------";
    }

    public String getTestStartedLog(String testDiscription, String testName) {

        return
                "\n _____ _____ ____ _____   ____ _____  _    ____ _____ _____ ____  \n" +
                        "|_   _| ____/ ___|_   _| / ___|_   _|/ \\  |  _ \\_   _| ____|  _ \\ \n" +
                        "  | | |  _| \\___ \\ | |   \\___ \\ | | / _ \\ | |_) || | |  _| | | | |\n" +
                        "  | | | |___ ___) || |    ___) || |/ ___ \\|  _ < | | | |___| |_| |\n" +
                        "  |_| |_____|____/ |_|   |____/ |_/_/   \\_\\_| \\_\\|_| |_____|____/ \n" +
                        "TEST STARTED: " + testDiscription + " \n" +
                        "----------------------------------------------------------------------("+ testName + ")";
    }

    public void getTestStatusLog(String testDiscription, String status) {

        String testPassed=
                "\n        __    _____ _____ ____ _____   ____   _    ____  ____  _____ ____  \n" +
                        "  _     \\ \\  |_   _| ____/ ___|_   _| |  _ \\ / \\  / ___|/ ___|| ____|  _ \\ \n" +
                        " (_)_____| |   | | |  _| \\___ \\ | |   | |_) / _ \\ \\___ \\\\___ \\|  _| | | | |\n" +
                        "  _|_____| |   | | | |___ ___) || |   |  __/ ___ \\ ___) |___) | |___| |_| |\n" +
                        " (_)     | |   |_| |_____|____/ |_|   |_| /_/   \\_\\____/|____/|_____|____/ \n" +
                        "        /_/                                                                \n" +
                        "TEST PASSED: " + testDiscription + " \n" +
                        "----------------------------------------------------------------------";

        String testFailed =
                "\n           __  _____ _____ ____ _____   _____ _    ___ _     _____ ____  \n" +
                        "  _       / / |_   _| ____/ ___|_   _| |  ___/ \\  |_ _| |   | ____|  _ \\ \n" +
                        " (_)_____| |    | | |  _| \\___ \\ | |   | |_ / _ \\  | || |   |  _| | | | |\n" +
                        "  _|_____| |    | | | |___ ___) || |   |  _/ ___ \\ | || |___| |___| |_| |\n" +
                        " (_)     | |    |_| |_____|____/ |_|   |_|/_/   \\_\\___|_____|_____|____/ \n" +
                        "          \\_\\                                                            \n" +
                        "TEST FAILED: " + testDiscription + " \n" +
                        "----------------------------------------------------------------------";

        String testError =
                "\n         __  _____ _____ ____ _____   _____ ____  ____   ___  ____  \n" +
                        " _      / / |_   _| ____/ ___|_   _| | ____|  _ \\|  _ \\ / _ \\|  _ \\ \n" +
                        "(_)____| |    | | |  _| \\___ \\ | |   |  _| | |_) | |_) | | | | |_) |\n" +
                        " |_____| |    | | | |___ ___) || |   | |___|  _ <|  _ <| |_| |  _ < \n" +
                        "(_)    | |    |_| |_____|____/ |_|   |_____|_| \\_\\_| \\_\\\\___/|_| \\_\\\n" +
                        "        \\_\\                                                         \n" +
                        "\n" +
                        "TEST ERROR: " + testDiscription + " \n" +
                        "----------------------------------------------------------------------";

        if(status.equals("PASSED")) {
            logger.info(testPassed);
        }
        else if(status.equals("FAILED")) {
            logger.info(testFailed);
        }
        else if(status.equals("ERROR")) {
            logger.info(testError);
        }
    }

}
