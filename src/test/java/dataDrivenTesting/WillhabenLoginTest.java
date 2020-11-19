package dataDrivenTesting;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pageFactory.WillhabenHomepage;
import pageFactory.WillhabenLoginPage;
import java.net.URL;
import java.sql.*;
import java.util.concurrent.TimeUnit;


public class WillhabenLoginTest {

    private AppiumDriver<WebElement> driver;
    private WillhabenHomepage willhabenHomepage;
    private WillhabenLoginPage willhabenLogin;
    private XSSFSheet sheet;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private WebDriverWait wait;

    /**
     * Verifying that one ot the 3 expected error alerts is displayed:
     *
     * @param element one of the three expected web elements (test features)
     */

    public void assertExpectedAlertIsDisplayed(AndroidElement element) {

        boolean statusElementDisplayed = false;

        try {
            statusElementDisplayed = this.wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(statusElementDisplayed);
    }


    /**
     * testSetup() performs:
     * setting up the capabilities,
     * establishing a connection to the appium server,
     * page objects initialization,
     *
     */

    @BeforeTest
    public void testSetup() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,"NEO");
        capabilities.setCapability(MobileCapabilityType.UDID,"5200a272b26823f7");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,"6.0.1");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "at.willhaben");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,"at.willhaben.MainActivity");

        try {
            URL url = new URL("http://127.0.0.1:4723/wd/hub");
            System.out.println("\nremotedriver NOK\n");/////////////
            this.driver = new AppiumDriver<>(url, capabilities);
            System.out.println("\nremotedriver ok\n");/////////
        } catch(Exception e) {
            System.out.println("Cause is: " + e.getCause());
            System.out.println("Error is: " + e.getMessage());
            e.printStackTrace();
        }

        //Page objects initialization
        this.willhabenLogin = new WillhabenLoginPage(this.driver);
        this.willhabenHomepage = new WillhabenHomepage(this.driver);
        this.wait = new WebDriverWait(this.driver, 60);

        //"Einloggen" element click on
        this.willhabenHomepage.clickLogin();

    }


    /**
     * The test method uses test data from a MySQL database - two pairs of nonvalid credentials but in correct format:
     *
     * [db1@gmx.at | password1]
     * [ db2@te.st | password2]
     *
     * Test feature - the following error alert:
     * "Die angegebene E-Mail-Adresse bzw. das Passwort konnten nicht erkannt werden."
     *
     */

    @Test
    public void loginUsingDB() {

        try {

            if (this.connection == null) {
                Class.forName("com.mysql.jdbc.Driver");
                this.connection = DriverManager
                        .getConnection("jdbc:mysql://localhost/db_login_data", "root", "");
            }

            this.statement = connection.createStatement();

            this.resultSet = statement.executeQuery("SELECT * FROM tb_login_data_valid_email_format");

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }

        // using data from the database

        try {
            while (this.resultSet.next()) {


                this.willhabenLogin.setEMail(this.resultSet.getString("username"));
                this.willhabenLogin.setPassword(this.resultSet.getString("password"));
                this.willhabenLogin.setloginButtonClick();

                //assert error message:
                //"Login fehlgeschlagen. Die angegebene E-Mail Adresse bzw. das Passwort konnten nicht erkannt werden."
                this.assertExpectedAlertIsDisplayed(this.willhabenLogin.getAlertEMailOderPasswortNichtErkannt());

                this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                this.willhabenLogin.clickOkButton();

            }
        } catch (SQLException e) {
            System.out.println("Fehler: " + e.getMessage());

        } finally {

            //this.driver.quit();

            try {
                if (this.statement != null) {
                    this.statement.close();
                }
                if (this.resultSet != null) {
                    this.resultSet.close();
                }
                if (this.connection != null) {
                    this.connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Fehler: " + e.getMessage());
            }
        }
    }

    @AfterTest
    public void tearDown() {
        this.driver.closeApp();
    }
}
