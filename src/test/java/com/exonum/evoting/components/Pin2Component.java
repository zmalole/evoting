package com.exonum.evoting.components;

import com.exonum.evoting.base.BaseTest;
import com.exonum.evoting.enums.LocatorType;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

import static com.exonum.evoting.components.AbstractComponent.ComponentActionButton.CANCEL;
import static com.exonum.evoting.components.AbstractComponent.ComponentActionButton.SIGN_BALLOT;

public class Pin2Component extends AbstractComponent {

    private static final String AREA_XPATH = "//div[contains(@class, 'modal-decrypt-dark')]";


    public Pin2Component(BaseTest testClass) {
        super(testClass, SIGN_BALLOT, CANCEL, AREA_XPATH);
    }


    private WebElement getPinDigit(int digit) {
        return testClass.findElement(LocatorType.XPATH,
                String.format("//div[contains(@class, 'keyboard-button') and ./div[.='%s']]", String.valueOf(digit)));
    }


    @Step("Sign ballot")
    public SignBallotComponent signBallot(int[] pin) {
        for (int p : pin) {
            testClass.waitTillClickableAndClickElement(getPinDigit(p));
        }
        return confirm(SignBallotComponent.class);
    }

}
