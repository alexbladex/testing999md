package org.example;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class TestItemsPage {
    private WebDriver driver;
    private MainPage mainpage;
    String uri, user, pswd;

    @BeforeClass
    public void setupTest() {
        uri = PropertyReader.getProperty("uri");
        user = PropertyReader.getProperty("user");
        pswd = PropertyReader.getProperty("pswd");
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        driver = DriverFactory.init();
        mainpage = new MainPage(driver, uri);
    }
    @AfterClass
    public void closeTest(){
        driver.quit();
    }
    @BeforeMethod
    public void initializePageObjects() {
        driver.get(uri);
    }
    @AfterMethod
    public void clearCookies(){
        driver.manage().deleteAllCookies();
    }
    @Test
    public void testDelLastItem() {
        LoginPage loginpage = new LoginPage(driver);
        loginpage.performLogin(user,pswd);
        UserItemsPage itempage = new UserItemsPage(driver);
        int adId = itempage.addDefaultAd();
        //String adId = itempage.getIdByTitle(adTitle);
        Assert.assertTrue(itempage.delLastItemById(adId), "Last Item was not deleted");
    }
}
