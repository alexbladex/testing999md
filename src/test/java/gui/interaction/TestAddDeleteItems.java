package gui.interaction;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

@Listeners(EventListener.class)
public class TestAddDeleteItems {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private WebDriver driver;
    String uri, user, pswd;

    @BeforeClass
    public void setupTest() {
        uri = PropertyReader.getProperty("uri");
        user = PropertyReader.getProperty("user");
        pswd = PropertyReader.getProperty("pswd");
        driver = DriverFactory.init();
        logger.info("Test setup complete");
    }
    @AfterClass
    public void closeTest(){
        DriverFactory.close();
        logger.info("Test Class closed");
    }
    //@Parameters("baseURL")
    @BeforeMethod
    public void initMainPage() {
        driver.get(uri);
        logger.info("Navigated to URL: {}", uri);
    }
    @BeforeMethod(onlyForGroups = "requiresLogin", dependsOnMethods = "initMainPage")
    //Methods with the same annotation, for ex. @BeforeMethod, are executed in the alphabetical order.
    //But attribute dependsOnMethods or Priority can change order
    public void performLogin() {
        LoginPage loginpage = new LoginPage(driver);
        Assert.assertTrue(loginpage.performLogin(user,pswd), "Login is not completed");
    }
    @AfterMethod
    public void clearCookies(){
        driver.manage().deleteAllCookies();
        logger.info("Cookies cleared");
    }
    @DataProvider(name = "ItemsToBeDeleted")
    public Object[][] sanityTestDataProvider() {
        //String[][] testData = {{ "размер" }, {"Размер"}, {"утреника"}};
        String[][] testData = {{ "размер" }};
        return testData;
    }
    @Test(enabled = true, groups = "requiresLogin", retryAnalyzer = RetryAnalyzer.class, dataProvider = "ItemsToBeDeleted", dependsOnMethods = "testAddAdData")
    public void testDelAllInactiveItems(String itemsSummary) {
        // this is methods depends on testAddAdData()
        // it is assumed that there are at least 9 ads in the marketplace with an inactive status
        // make sure that the title of the ads created in the testAddAdData method contains the keyword from dataProvider
        // for example, when creating an ad via addDefaultAd() with the title Toyota, make sure that the same word is exist in the dataProvider
        DelAdPage itempage = new DelAdPage(driver);
        int i = itempage.delInactiveItems(itemsSummary);
        Assert.assertTrue(i >= 9, "Not all ads successfully deleted from the marketplace");
    }
    @Test(enabled = true, groups = "requiresLogin", retryAnalyzer = RetryAnalyzer.class)
    public void testAddAdData() {
        // it is assumed that the marketplace places only one ad, all the others will be in an inactive status
        DelAdPage itempage = new DelAdPage(driver);
        int inactive = 0;
        for (int i = 0; i < 10; i++) {
            AdItem ad = itempage.addDefaultAd();
            if (ad.getStatus().matches("need_pay|expired|blocked")) inactive++;
        }
        System.out.println("Was created inactive ads: " + inactive);
        Assert.assertTrue(inactive >= 9, "Not all ads successfully placed to the marketplace");
    }
}