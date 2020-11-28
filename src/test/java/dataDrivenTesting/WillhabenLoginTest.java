package dataDrivenTesting;

/*
 * For demonstration purposes the following class contains 3 negative login tests (TC_Login_02 - TC_Login_04) using
 * test data from 3 different sources.
 * For more details concerning the test cases, see the Test_Plan_WiHa.xlsx.
 */

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pageFactory.WillhabenHomepage;
import pageFactory.WillhabenLoginPage;
import java.io.File;
import java.io.IOException;
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
    private DesiredCapabilities capabilities;

    /**
     * Verifying that one of the 3 expected error messages is displayed:
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
     * setting up the capabilities and
     * importing data from an excel file.
     */

    @BeforeTest
    public void testSetup() {
        this.capabilities = new DesiredCapabilities();
        this.capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,"NEO"); //you have to add your own settings
        this.capabilities.setCapability(MobileCapabilityType.UDID,"5200a272b26823f7"); //you have to add your own settings
        this.capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android"); //you have to add your own settings
        this.capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,"6.0.1"); //you have to add your own settings
        this.capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "at.willhaben");
        this.capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,"at.willhaben.MainActivity");

        //importing data from an excel file
        try {
            File src = new File(".\\src\\main\\java\\testdataAndDriver\\Testdata_Excel.xlsx");
            OPCPackage pkg = OPCPackage.open(src);
            XSSFWorkbook workbook = new XSSFWorkbook(pkg);
            this.sheet = workbook.getSheetAt(0);
            pkg.close();
        } catch (IOException | InvalidFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @AfterTest
    public void tearDown() {
        if (this.driver != null) {
            this.driver.closeApp();
        }
    }


    /**
     * testMethodSetup() performs:
     * establishing a connection to the Appium server,
     * page objects initialization,
     * a click on the "Einloggen" web element.
     */

    @BeforeMethod
    public void testMethodSetup() {
        try {
            URL url = new URL("http://127.0.0.1:4723/wd/hub");
            this.driver = new AppiumDriver<>(url, this.capabilities);
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

    /*
      TC_Login_01 - "Happy Path" Test case is NOT IMPLEMENTED here, since correct login credentials are required for this!!!
     */


    /**
     * Test case - TC_Login_02
     * Test feature - user login and the error message:
     * "Die angegebene E-Mail-Adresse bzw. das Passwort konnten nicht erkannt werden".
     *
     * This parametrized TestNG negative login test uses following test data (with nonvalid credentials) from the TESTNG.XML:     *
     * @param email par@metriz.ed
     * @param password parameter
     */

    @Test
    @Parameters({"e-mail", "password"})
    public void testParametrized(String email, String password) {

        this.willhabenLogin.setEMail(email);
        this.willhabenLogin.setPassword(password);
        this.willhabenLogin.setloginButtonClick();

        //verifying the error message: "Die angegebene E-Mail Adresse bzw. das Passwort konnten nicht erkannt werden."
        this.assertExpectedAlertIsDisplayed(this.willhabenLogin.getAlertEMailOderPasswortNichtErkannt());

        this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        this.willhabenLogin.clickOkButton();
    }


    /**
     * Test case - TC_Login_03
     * Test feature - user login and the error message: "Passwort".
     * These negative test imports and uses test data from an EXCEL file:
     * logon information WITH e-mail address but WITHOUT a password.
     */

    @Test
    public void testLoginUsingExcelFileData_noPassword() {

        this.willhabenLogin.setInputFieldEMail(this.sheet.getRow(1).getCell(0).getStringCellValue());

        //submit
        this.willhabenLogin.setloginButtonClick();

        //verifying the error message: "Passwort"
        this.assertExpectedAlertIsDisplayed(this.willhabenLogin.getInputFieldPassword());
    }


    /**
     * Test case - TC_Login_04
     * Test feature - user login and the error message: "E-Mail-Adresse".
     *
     * The negative test uses test data from a MySQL database - logon information WITH password but WITHOUT an e-mail address:
     * [    | password_DB].
     */

    @Test
    public void testLoginUsingDB() {

        try {

            if (this.connection == null) {
                Class.forName("com.mysql.jdbc.Driver");
                this.connection = DriverManager
                        .getConnection("jdbc:mysql://localhost/db_login_data", "root", "");
            }
            this.statement = connection.createStatement();
            this.resultSet = statement.executeQuery("SELECT * FROM tb_login_data_appium");

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // using data from the database

        try {
            while (this.resultSet.next()) {

                this.willhabenLogin.setPassword(this.resultSet.getString("password"));
                this.willhabenLogin.setloginButtonClick();

                //verifying the error message:
                //"Login fehlgeschlagen. Die angegebene E-Mail Adresse bzw. das Passwort konnten nicht erkannt werden."
                this.assertExpectedAlertIsDisplayed(this.willhabenLogin.getInputFieldEMail());
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());

        } finally {
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
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
