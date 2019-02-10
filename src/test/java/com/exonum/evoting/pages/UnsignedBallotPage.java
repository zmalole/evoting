package com.exonum.evoting.pages;

import com.exonum.evoting.base.BaseTest;
import com.exonum.evoting.enums.UnsignedBallotButton;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class UnsignedBallotPage extends BasePage {

    private static final String FIELD_PREF_XPATH = "//div[contains(@class, 'inner-header') and .='";
    private static final String FIELD_SUFF_XPATH = "']/following-sibling::div[contains(@class, 'code-box')][1]";

    @FindBy(xpath = FIELD_PREF_XPATH + "Ballot reciept 3-word memo" + FIELD_SUFF_XPATH)
    private WebElement ballotReciept3WordMemoField;

    @FindBy(xpath = FIELD_PREF_XPATH + "Ballot  SHA256 hash" + FIELD_SUFF_XPATH)
    private WebElement ballotSha256HashField;

    @FindBy(xpath = "//div[@class='buttons-wrapper']//div[contains(@class, 'button') and not(./*)]")
    private List<WebElement> buttons;


    public UnsignedBallotPage(BaseTest testClass) {
        super(testClass);
    }


    @Step("Get Ballot reciept 3-word memo")
    public String getBallotReciept3WordMemoFieldText() {
        testClass.waitTillElementIsVisible(ballotReciept3WordMemoField);
        return ballotReciept3WordMemoField.getText();
    }

    @Step("Get Ballot  SHA256 hash")
    public String getBallotSha256HashFieldText() {
        testClass.waitTillElementIsVisible(ballotSha256HashField);
        return ballotSha256HashField.getText();
    }

    @Step("Get button labels")
    public List<String> getButtonLabels() {
        return getNameList(buttons);
    }

    public <T extends AbstractPage> T clickButton(UnsignedBallotButton button, Class<T> pageToReturn) {
        for (WebElement b : buttons) {
            testClass.waitTillElementIsVisible(b);
            if (b.getText().equals(button.getLabel().toUpperCase())) {
                testClass.scrollToElementByJsAndClick(b);
                break;
            }
        }
        return createPage(pageToReturn);
    }

}
