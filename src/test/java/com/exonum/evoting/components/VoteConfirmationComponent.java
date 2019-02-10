package com.exonum.evoting.components;

import com.exonum.evoting.base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.exonum.evoting.components.AbstractComponent.ComponentActionButton.*;
import static org.testng.Assert.assertEquals;

public class VoteConfirmationComponent extends AbstractComponent {

    @FindBy(xpath = "//div[@class='confirm-choise-block-title']")
    private WebElement title;

    @FindBy(xpath = "//div[contains(@class, 'name')]")
    private WebElement candidateName;


    public VoteConfirmationComponent(BaseTest testClass) {
        super(testClass, YES, CANCEL);
    }

    public VoteConfirmationComponent(BaseTest testClass, String candidateName) {
        super(testClass, YES, CANCEL);
        verifyComponentIsLoaded(candidateName);
    }


    @Step("Get candidate name")
    public String getCandidateName() {
        testClass.waitTillElementIsVisible(candidateName);
        return candidateName.getText();
    }

    @Step("Verify confirmation title")
    public void verifyTitle() {
        testClass.waitTillElementIsVisible(title);
        assertEquals(title.getText(),
                "Are you sure you want to prepare\n" +
                        "an anonymous ballot with\n" +
                        "following candidate selected?",
                "Incorrect confirmation message is shown");
    }

    @Step("Verify candidate name")
    public void verifyCandidateName(String candidateName) {
        assertEquals(getCandidateName(), candidateName, "Unexpected candidate name");
    }

    public void verifyComponentIsLoaded(String candidateName) {
        verifyTitle();
        verifyCandidateName(candidateName);
    }

}
