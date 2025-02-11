package com.nopCommerce.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryMethod implements IRetryAnalyzer {

    /**
     * This method determines how many times a test should be retried.
     * TestNG will call this method every time a test fails, allowing you to decide when to re-run the test.
     * <p>
     * Note: This method returns `true` if the test needs to be retried, and `false` if it does not.
     * <p>
     * "iTestResult" The result of the test method that was executed.
     * "boolean" indicating whether the test should be retried.
     */


    private final ThreadLocal<Integer> count = ThreadLocal.withInitial(() -> 0);
    private final int maxTry= Integer.parseInt(ReadFiles.config.getProperty("maxRetryLimit"));
    private final Logger logger= LogManager.getLogger("RetryMethod.class");

    @Override
    public boolean retry(ITestResult iTestResult) {

        if(! iTestResult.isSuccess()){

            // Skip retries for assertion failures...
            if(iTestResult.getThrowable() instanceof AssertionError){
                logger.warn("Skipping retry for assertion failure: {}", iTestResult.getMethod().getMethodName());
                return false;
            }

            // Retry logic for other exceptions...
            if (count.get() < maxTry) {
                count.set(count.get() + 1);
                logger.info("Retrying test: {} | Attempt: {}/{}", iTestResult.getMethod().getMethodName(), count.get(), maxTry);
                return true;
            }
            else
                logger.info("Max retry limit reached for test: {}", iTestResult.getMethod().getMethodName());
        }
        return false;
    }
}
