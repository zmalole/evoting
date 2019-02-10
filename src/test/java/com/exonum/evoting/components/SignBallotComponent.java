package com.exonum.evoting.components;

import com.exonum.evoting.base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.exonum.evoting.components.AbstractComponent.ComponentActionButton.DISCARD_BALLOT;
import static com.exonum.evoting.components.AbstractComponent.ComponentActionButton.SUBMIT_BALLOT;

public class SignBallotComponent extends AbstractComponent {

    @FindBy(xpath = "//div[contains(@class, 'input') and .='E-mail:']/following-sibling::input[@placeholder='Your mail']")
    private WebElement emailInput;


    public SignBallotComponent(BaseTest testClass) {
        super(testClass, SUBMIT_BALLOT, DISCARD_BALLOT);
    }


    @Step("Set email address to the input field")
    public void setEmail(String email) {
        testClass.waitTillElementIsClickable(emailInput);
        typeIn(emailInput, email);
    }

}
