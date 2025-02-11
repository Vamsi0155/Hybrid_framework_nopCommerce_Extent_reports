package com.nopCommerce.testCases;

import com.nopCommerce.factory.ExcelFiles;
import com.nopCommerce.factory.ReadFiles;
import com.nopCommerce.utils.BaseClass;
import com.nopCommerce.utils.CommonMethods;
import com.nopCommerce.utils.PageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;

public class RegistrationTest extends BaseClass {

    private PageManager pages;
    private final Logger logger = LogManager.getLogger("RegistrationTest.class");

    @BeforeMethod
    public void loadUrl(){

        pages = new PageManager(getDriver());
        pages.homePage().openUrl(loader.getUrl());
        logger.info("Url is opened");
    }

    @Test(description = "Register a new user", dataProvider = "RegistrationData", priority = 1)
    public void registerAUser(Map<String, String> input, ITestContext context){
        input = CommonMethods.parseGivenInput(input);

        try{
            pages.homePage().verifyHomePageTitle();
            pages.homePage().pageScrollBottom();
            pages.homePage().clickOnBookStoreApp();

            pages.bookStorePage().verifyBSPageTitle();
            pages.bookStorePage().clickOnLoginOfMenu();
            pages.bookStorePage().clickOnNewUser();

            pages.bookStorePage().verifyRegisterTitle();
            pages.bookStorePage().enterNewPassword(input.get("Password"));
            pages.bookStorePage().enterNewUserName(input.get("User_Name"));
            pages.bookStorePage().enterFirstName(input.get("First_Name"));
            pages.bookStorePage().enterLastName(input.get("Last_Name"));

            pages.bookStorePage().clickOnCaptcha();
            pages.bookStorePage().clickOnRegister();
            String msg = pages.bookStorePage().getAlertMessage();
            Assert.assertEquals(pages.bookStorePage().getAlertMessage(), "User Register Successfully.", (input.get("User_Name")+" user not registered"));
            pages.bookStorePage().acceptAlert();
            logger.info("A new user is created successfully");
            loader.setRegisteredCredentials(input);
            printResults(input.get("User_Name"), input.get("Password"));
        }
        catch (Exception e){
            loader.setFlag(loader.getFlag()+1);
            if(e.getCause() instanceof AssertionError){
                loader.setFlag(0);
                context.setAttribute("FailedUser", input.get("User_Name"));
                logger.info("A new user onboarding failed");
                printResults(input.get("User_Name"), input.get("Password"));
            }
            else if(loader.getFlag()==2) {
                loader.setFlag(0);
                context.setAttribute("FailedUser", input.get("User_Name"));
                logger.info("A new user onboarding by error");
                printResults(input.get("User_Name"), input.get("Password"));
            }
            throw e;
        }
    }

    @DataProvider(name="RegistrationData", parallel = false)
    public Object[][] getRegistrationData(){

        String path = ReadFiles.getPath("RegistrationData");
        int rowCount = ExcelFiles.getRowCount(path, "Sheet1");
        int colCount = ExcelFiles.getCellCount(path, "Sheet1", rowCount);

        List<Map<String, String>> data = new ArrayList<>();
        for (int i = 1; i <=rowCount; i++) {
            Map<String, String> temp = new HashMap<>();
            for (int j = 0; j < colCount; j++) {
                temp.put(ExcelFiles.getCellData(path, "Sheet1", 0,j), ExcelFiles.getCellData(path, "Sheet1", i,j));
            }
            data.add(temp);
        }

        Object[][] dataArray = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            dataArray[i][0] = data.get(i);
        }
        return dataArray;
    }


    @Test(description="Newly registered users validation by login into store", dataProvider="NewUserData", priority=2)
    public void validateLoginFunctionWithNewUsers(Map<String, String> credentials, ITestContext context) {

        if(credentials.isEmpty()){
            Assert.fail("no user details found");
        }
        try{
            pages.homePage().verifyHomePageTitle();
            pages.homePage().pageScrollBottom();
            pages.homePage().clickOnBookStoreApp();

            pages.bookStorePage().verifyBSPageTitle();
            pages.bookStorePage().clickOnLoginOfMenu();

            pages.bookStorePage().enterUserName(credentials.get("User_Name"));
            pages.bookStorePage().enterPassword(credentials.get("Password"));
            pages.bookStorePage().clickOnLoginBtn();
            pages.homePage().applyForceWait(2000);

            String user_Name = pages.bookStorePage().getUserName();
            Assert.assertEquals(user_Name, credentials.get("User_Name"), "Unknown user name was found!!");

            pages.bookStorePage().clickOnLogout();
            logger.info("New user able to login into store successfully");
            printResults(credentials.get("User_Name"), credentials.get("Password"));
        }
        catch (Exception e){
            loader.setFlag(loader.getFlag()+1);
            if(e.getCause() instanceof AssertionError){
                loader.setFlag(0);
                context.setAttribute("FailedUser", credentials.get("User_Name"));
                logger.info("A new user failed to login into store");
                printResults(credentials.get("User_Name"), credentials.get("Password"));
            }
            else if(loader.getFlag()==2) {
                loader.setFlag(0);
                context.setAttribute("FailedUser", credentials.get("User_Name"));
                logger.info("A new user failed to login by error");
                printResults(credentials.get("User_Name"), credentials.get("Password"));
            }
            throw e;
        }
    }

    @DataProvider(name = "NewUserData")
    public Object[][] getNewUserData(){

        Object[][] data = new Object[loader.getRegisteredCredentials().size()][1];
        for (int i = 0; i < loader.getRegisteredCredentials().size(); i++) {
            data[i][0] = loader.getRegisteredCredentials().get(i);
        }
        return data;
    }

    public void printResults(String user, String password){
        logger.info(String.format("| %-15s | %-19s |", "User Name", "Password"));
        logger.info("+-----------------+---------------------+");
        logger.info(String.format("| %-15s | %-19s |", user, password));
    }
}
