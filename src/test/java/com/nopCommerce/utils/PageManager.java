package com.nopCommerce.utils;

import com.nopCommerce.pageObjects.BookStorePage;
import com.nopCommerce.pageObjects.HomePage;
import org.openqa.selenium.WebDriver;

public class PageManager {

    private final WebDriver driver;

    public PageManager(WebDriver driver){
        this.driver = driver;
    }

    public HomePage homePage(){
        return new HomePage(driver);
    }

    public BookStorePage bookStorePage(){
        return new BookStorePage(driver);
    }

}
