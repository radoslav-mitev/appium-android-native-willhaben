package pageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class WillhabenHomepage {


    @AndroidFindBy (id = "at.willhaben:id/btn_onboarding_login")
    AndroidElement loginButton;

    public WillhabenHomepage(AppiumDriver<WebElement> driver){
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    //Click on "Einloggen" link
    public void clickLogin(){
        this.loginButton.click();
    }


}
