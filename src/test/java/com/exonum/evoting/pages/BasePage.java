package com.exonum.evoting.pages;

import com.exonum.evoting.base.BaseTest;
import io.qameta.allure.Step;
import lombok.extern.java.Log;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Log
public abstract class BasePage extends AbstractPage {

    @FindBy(xpath = "//div[contains(@class, 'button-red') and .='VOTE IN ELECTION']")
    protected WebElement voteButton;


    public BasePage(BaseTest testClass) {
        super(testClass);
    }


    public <T extends AbstractPage> T voteInElectionClick(Class<T> pageToReturn) {
        return voteInElectionClick(pageToReturn, null);
    }

    @Step("Click on 'VOTE IN ELECTION' button")
    public <T extends AbstractPage> T voteInElectionClick(Class<T> pageToReturn, String param) {
        testClass.waitTillElementIsClickable(voteButton);
        log.info("click on <" + voteButton.getText() + "> button");
        voteButton.click();
        return createPage(pageToReturn, param);
    }

}
