package com.exonum.evoting.pages;

import com.exonum.evoting.base.BaseTest;
import lombok.extern.java.Log;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Log
public class MainPage extends BasePage {

    private static final String WELCOME_XPATH = "//div[@class='welcome-intro']";

    @FindBy(xpath = WELCOME_XPATH + "/div[@class='app-logo']")
    private WebElement logo;

    @FindBy(xpath = WELCOME_XPATH + "/div[@class='app-header' and .='e-Voting']")
    private WebElement header;

    @FindBy(xpath = WELCOME_XPATH + "/div[@class='app-subheader' and .='App for elections']")
    private WebElement subheader;


    public MainPage(BaseTest testClass) {
        super(testClass);
    }

}
