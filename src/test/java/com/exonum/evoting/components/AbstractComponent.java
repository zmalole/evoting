package com.exonum.evoting.components;

import com.exonum.evoting.base.BaseTest;
import com.exonum.evoting.enums.LocatorType;
import com.exonum.evoting.pages.AbstractPage;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.WebElement;

public abstract class AbstractComponent extends AbstractPage {

    private static final String ACTION_BTN_XPATH = "//div[contains(@class, 'button') and .='%s']";

    private final String areaXpath;

    private WebElement confirmButton;

    private WebElement closeButton;


    public AbstractComponent(BaseTest testClass, ComponentActionButton confirm, ComponentActionButton cancel) {
        super(testClass);
        areaXpath = "";
        confirmButton = getActionBtn(confirm);
        closeButton = getActionBtn(cancel);
    }

    public AbstractComponent(BaseTest testClass, ComponentActionButton confirm, ComponentActionButton cancel, String areaXpath) {
        super(testClass);
        this.areaXpath = areaXpath;
        confirmButton = getActionBtn(confirm);
        closeButton = getActionBtn(cancel);
    }


    private WebElement getActionBtn(ComponentActionButton button) {
        return testClass.findElement(LocatorType.XPATH, String.format(areaXpath + ACTION_BTN_XPATH, button.getLabel()));
    }


    @AllArgsConstructor
    @Getter
    protected enum ComponentActionButton {
        YES("YES"),
        CANCEL("CANCEL"),
        SIGN_BALLOT("SIGN BALLOT"),
        SUBMIT_BALLOT("SUBMIT BALLOT"),
        DISCARD_BALLOT("DISCARD BALLOT");

        private final String label;
    }


    @Step("Click confirmation button")
    public <T extends AbstractPage> T confirm(Class<T> pageToReturn) {
        testClass.waitTillClickableAndClickElement(confirmButton);
        return createPage(pageToReturn);
    }

    @Step("Click close button")
    public <T extends AbstractPage> T close(Class<T> pageToReturn) {
        testClass.waitTillClickableAndClickElement(closeButton);
        return createPage(pageToReturn);
    }

}
