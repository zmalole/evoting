package com.exonum.evoting.pages;

import com.exonum.evoting.base.BaseTest;
import com.exonum.evoting.enums.LocatorType;
import io.qameta.allure.Step;
import lombok.extern.java.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Log
public abstract class AbstractListPage extends BasePage {

    protected final static String TABLE_ROWS_XPATH = "//table//tr";

    @FindBy(xpath = TABLE_ROWS_XPATH)
    protected List<WebElement> tableRows;


    public AbstractListPage(BaseTest testClass) {
        super(testClass);
    }


    protected WebElement getTableRow(String text) {
        return testClass.findElement(LocatorType.XPATH, String.format("%s[contains(., '%s')]", TABLE_ROWS_XPATH, text));
    }


    @Step("Select from table and verify row is selected")
    public void selectFromTable(String text) {
        WebElement tableRow = getTableRow(text);
        testClass.waitTillClickableAndClickElement(tableRow);
        String rowText = tableRow.getText();
        log.info("clicked on <" + rowText + "> row");
        testClass.waitTillElementIsVisible(tableRow.findElement(By.xpath(".//div[@class='checker active']")));
        log.info("verified <" + rowText + "> row is selected ");
    }

    @Step("Get number of rows in the table")
    public int getNumberOfRows() {
        testClass.waitTillElementsAreVisible(tableRows);
        int numOfRows = tableRows.size();
        log.info("table size = " + numOfRows);
        return numOfRows;
    }

}
