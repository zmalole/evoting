package com.exonum.evoting.pages;

import com.exonum.evoting.base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CandidatesOfElectionPage extends ElectionListPage {

    @FindBy(xpath = "//a[@class='list-option-link' and .='Official candidate page']")
    private WebElement candidateWebPageLink;

    @FindBy(xpath = "//div[contains(@class, 'list-option-description')]")
    private WebElement candidateInfo;


    public CandidatesOfElectionPage(BaseTest testClass) {
        super(testClass);
    }


    @Step("Get candidate web page link")
    public String getCandidateWebPageLink() {
        testClass.waitTillElementIsVisible(candidateWebPageLink);
        return candidateWebPageLink.getAttribute("href");
    }

    @Step("Get candidate info")
    public String getCandidateInfo() {
        testClass.waitTillElementIsVisible(candidateInfo);
        return candidateInfo.getText();
    }

}
