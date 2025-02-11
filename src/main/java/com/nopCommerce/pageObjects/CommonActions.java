package com.nopCommerce.pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CommonActions {

    private final WebDriver driver;

    public CommonActions(WebDriver rdriver){
        this.driver = rdriver;
    }

    public void openUrl(String url){
        driver.get(url);
    }

    public void refreshPage(){
        driver.navigate().refresh();
    }

    public void closeBrowser(){
        driver.close();
    }

    public void pageScrollBottom(){
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void pageScrollTop(){
        doAction().keyDown(Keys.CONTROL).sendKeys(Keys.HOME).keyUp(Keys.CONTROL).perform();
    }

    public void scrollUntilView(WebElement element){
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public Actions doAction() {
        return new Actions(driver);
    }

    public Select doSelect(WebElement dropDown) {
        return new Select(dropDown);
    }

    public void acceptAlert(){
        driver.switchTo().alert();
        driver.switchTo().alert().accept();
    }

    public void dismissAlert(){
        driver.switchTo().alert().dismiss();
    }

    public String getAlertMessage(){
        doWait().until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert().getText();
    }

    public void applyForceWait(long sec){
        try {
            Thread.sleep(sec);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public WebDriverWait doWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(20));
    }

}
