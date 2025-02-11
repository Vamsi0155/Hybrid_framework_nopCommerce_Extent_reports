package com.nopCommerce.testCases;

import com.nopCommerce.utils.BaseClass;
import com.nopCommerce.utils.PageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserValidation extends BaseClass {

    private PageManager pages;
    private final Logger logger = LogManager.getLogger("UserValidation.class");

    @BeforeMethod
    public void loadUrl(){

        pages = new PageManager(getDriver());
        pages.homePage().openUrl(loader.getUrl());
        logger.info("Url is opened");
    }

    @Test(description = "Book store login functionality validation")
    public void validateLoginFunction() {

        pages.homePage().verifyHomePageTitle();
        pages.homePage().pageScrollBottom();
        pages.homePage().clickOnBookStoreApp();

        pages.bookStorePage().verifyBSPageTitle();
        pages.bookStorePage().clickOnLoginOfMenu();

        pages.bookStorePage().enterUserName(loader.getUserId());
        pages.bookStorePage().enterPassword(loader.getPassword());
        pages.bookStorePage().clickOnLoginBtn();

        String user_Name = pages.bookStorePage().getUserName();
        Assert.assertEquals(user_Name, loader.getUserId(), "Unknown user name was found!!");

        pages.bookStorePage().clickOnLogout();
        pages.homePage().applyForceWait(2000);
        logger.info("Login function is validated successfully");
    }

}
