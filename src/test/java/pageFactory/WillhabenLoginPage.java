package pageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class WillhabenLoginPage {

    @AndroidFindBy(id = "at.willhaben:id/edittextScreenLoginEmail")
    private AndroidElement inputFieldEMail;

    public AndroidElement getInputFieldEMail() {
        return this.inputFieldEMail;
    }

    public void setInputFieldEMail(String eMail) {
        this.inputFieldEMail.sendKeys(eMail);
    }


    @AndroidFindBy(id = "at.willhaben:id/edittextScreenLoginPassword")
    private AndroidElement inputFieldPassword;

    public AndroidElement getInputFieldPassword() {
        return this.inputFieldPassword;
    }

    public void setInputFieldPassword(String inputFieldPassword) {
        this.inputFieldPassword.sendKeys(inputFieldPassword);
    }


    @AndroidFindBy(id = "at.willhaben:id/btnScreenLogin")
    private AndroidElement loginButton;

    public void setloginButtonClick() {
        this.loginButton.click();
    }

    //error message:
    //"Die angegebene E-Mail-Adresse bzw. das Passwort konnten nicht erkannt werden.
    @AndroidFindBy(id = "at.willhaben:id/dialog_message")
    private AndroidElement alertEMailOderPasswortNichtErkannt;

    public AndroidElement getAlertEMailOderPasswortNichtErkannt() {
        return this.alertEMailOderPasswortNichtErkannt;
    }

    @AndroidFindBy(id = "at.willhaben:id/dialog_button_confirm")
    private AndroidElement okButton;

    public void clickOkButton() {
        this.okButton.click();
    }

    public void setEMail(String eMail) {
        this.inputFieldEMail.sendKeys(eMail);
    }

    public void setPassword(String inputFieldPassword) {
        this.inputFieldPassword.sendKeys(inputFieldPassword);
    }


    public WillhabenLoginPage (AppiumDriver<WebElement> driver){
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

}
