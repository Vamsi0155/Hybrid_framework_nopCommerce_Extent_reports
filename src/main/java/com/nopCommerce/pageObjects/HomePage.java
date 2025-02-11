package com.nopCommerce.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class HomePage extends CommonActions{

    WebDriver driver;

    public HomePage(WebDriver rdriver) {
        super(rdriver);
        this.driver = rdriver;
    }

    By pageTitle = By.xpath("//img[@src='/images/Toolsqa.jpg']");
    By bookStoreApp = By.xpath("//h5[normalize-space()='Book Store Application']");

    public void verifyHomePageTitle(){
        doWait().until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
        Assert.assertTrue(driver.findElement(pageTitle).isDisplayed(), "Home page title is not displayed");
    }

    public void clickOnBookStoreApp(){
        driver.findElement(bookStoreApp).click();
    }

}
