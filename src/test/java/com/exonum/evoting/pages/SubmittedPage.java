package com.exonum.evoting.pages;

import com.exonum.evoting.base.BaseTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SubmittedPage extends BasePage {

    @FindBy(xpath = "//div[@class='grats-title' and .='Congratulations!']")
    private WebElement congratulationsLabel;


    public SubmittedPage(BaseTest testClass) {
        super(testClass);
    }

}
