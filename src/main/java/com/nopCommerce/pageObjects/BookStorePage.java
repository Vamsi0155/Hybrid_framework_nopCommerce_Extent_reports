package com.nopCommerce.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class BookStorePage extends CommonActions {

    WebDriver driver;

    public BookStorePage(WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    By pageTitle = By.xpath("//img[@src='/images/Toolsqa.jpg']");
    By loginMenu = By.xpath("//span[text()='Login']");
    By userName = By.id("userName");
    By password = By.id("password");
    By loginBtn = By.id("login");
    By userNameText = By.id("userName-value");
    By logoutBtn = By.xpath("//label[@id='userName-value']/following-sibling::button");
    By newUserBtn = By.id("newUser");

    By registerTitle = By.xpath("//h1[normalize-space()='Register']");
    By firstName = By.id("firstname");
    By lastName = By.id("lastname");
    By newUserName = By.id("userName");
    By newPassword = By.id("password");
    By registerBtn = By.id("register");
    By reCaptcha = By.xpath("//span[@id='recaptcha-anchor']");

    public void verifyBSPageTitle(){
        doWait().until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
        Assert.assertTrue(driver.findElement(pageTitle).isDisplayed(), "Book store page title is not displayed");
    }

    public void clickOnLoginOfMenu(){
        scrollUntilView(driver.findElement(loginMenu));
        driver.findElement(loginMenu).click();
    }

    public void enterUserName(String userId){
        doWait().until(ExpectedConditions.visibilityOfElementLocated(userName));
        driver.findElement(userName).sendKeys(Keys.END);
        driver.findElement(userName).sendKeys(userId);
    }

    public void enterPassword(String userPassword){
        driver.findElement(password).sendKeys(userPassword);
    }

    public void clickOnLoginBtn(){
        driver.findElement(loginBtn).sendKeys(Keys.END);
        driver.findElement(loginBtn).click();
    }

    public String getUserName(){
        doWait().until(ExpectedConditions.visibilityOfElementLocated(userNameText));
        return driver.findElement(userNameText).getText();
    }

    public void clickOnLogout(){
        driver.findElement(logoutBtn).sendKeys(Keys.END);
        driver.findElement(logoutBtn).click();
    }

    public void clickOnNewUser(){
        doWait().until(ExpectedConditions.visibilityOfElementLocated(newUserBtn));
        driver.findElement(newUserBtn).sendKeys(Keys.END);
        driver.findElement(newUserBtn).click();
    }

    public void verifyRegisterTitle(){
        doWait().until(ExpectedConditions.visibilityOfElementLocated(registerTitle));
        Assert.assertTrue(driver.findElement(registerTitle).isDisplayed(), "Register page title is not displayed");
    }

    public void enterFirstName(String fname){
        driver.findElement(firstName).sendKeys(fname);
    }

    public void enterLastName(String lname){
        driver.findElement(lastName).sendKeys(lname);
    }

    public void enterNewUserName(String userName){
        driver.findElement(newUserName).sendKeys(userName);
    }

    public void enterNewPassword(String password){
        driver.findElement(newPassword).sendKeys(Keys.END);
        driver.findElement(newPassword).sendKeys(password);
    }

    public void clickOnRegister(){
        driver.switchTo().defaultContent();
        driver.findElement(registerBtn).click();
    }

    public void verifyRegisterAlertMessage(){
        driver.switchTo().alert();

        Assert.assertEquals(driver.switchTo().alert().getText(),"User Register Successfully.", "Alert message is not verified");
        driver.switchTo().alert().accept();
    }

    public void clickOnCaptcha(){
        String iframeName = driver.findElement(By.tagName("iframe")).getAttribute("name");
        doWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//iframe[starts-with(@name,'a-')]")));
        doWait().until(ExpectedConditions.elementToBeClickable(reCaptcha));
        driver.findElement(reCaptcha).click();
        driver.switchTo().defaultContent();
        applyForceWait(4000);
    }

}
