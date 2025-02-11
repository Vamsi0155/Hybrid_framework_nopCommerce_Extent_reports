package com.nopCommerce.factory;


import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Loader {

    private static volatile Loader instance;

    private String region;
    private final ThreadLocal<String> browser = new ThreadLocal<>();

    private String url;
    private String userName;
    private String userId;
    private String password;
    private String currency;

    private final List<Map<String, String>> registeredCredentials;
    private int flag=0;

    private final ChromeOptions chromeOptions;
    private final EdgeOptions edgeOptions;
    private final FirefoxOptions firefoxOptions;

    private Loader(){
        chromeOptions = new ChromeOptions();
        edgeOptions = new EdgeOptions();
        firefoxOptions = new FirefoxOptions();
        registeredCredentials = new ArrayList<>();
    }

    public static Loader getLoader(){
        if(instance ==null){
            synchronized (Loader.class){
                if(instance == null){
                    instance = new Loader();
                }
            }
        }
        return instance;
    }

    public void setChromeOptions(String arguments){
        chromeOptions.addArguments(arguments);
    }
    public ChromeOptions getChromeOptions(){
        return chromeOptions;
    }

    public void setEdgeOptions(String arguments){
        edgeOptions.addArguments(arguments);
    }
    public EdgeOptions getEdgeOptions(){
        return edgeOptions;
    }
    public void setFirefoxOptions(String arguments){
        firefoxOptions.addArguments(arguments);
    }

    public FirefoxOptions getFirefoxOptions(){
        return firefoxOptions;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBrowser() {
        return browser.get();
    }

    public void removeBrowser(){
        browser.remove();
    }

    public void setBrowser(String browser) {
        this.browser.set(browser);
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Map<String, String>> getRegisteredCredentials() {
        return registeredCredentials;
    }

    public void setRegisteredCredentials(Map<String, String> credentials) {
        this.registeredCredentials.add(credentials);
    }

    public void setFlag(int num){
        this.flag = num;
    }
    public int getFlag(){
        return flag;
    }

}
