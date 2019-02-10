package com.exonum.evoting.pages;

import com.exonum.evoting.base.BaseTest;
import com.exonum.evoting.enums.ElementAttribute;
import io.qameta.allure.Step;
import lombok.extern.java.Log;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Log
public abstract class AbstractPage {

    protected BaseTest testClass;


    public AbstractPage(BaseTest testClass) {
        log.info(String.format(">>> Initializing <%s> <<<", this.getClass().getSimpleName()));
        this.testClass = testClass;
        waitForLoad();
        PageFactory.initElements(this.testClass.getWebDriver(), this);
    }


    protected <T extends AbstractPage> T createPage(Class<T> clazz) {
        return createPage(clazz, null);
    }

    protected <T extends AbstractPage> T createPage(Class<T> clazz, String param) {
        if (param != null)
            try {
                return clazz.getConstructor(BaseTest.class, String.class).newInstance(testClass, param);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Cannot create object of class: " + clazz.getName() + ", with param: " + param);
            }
        else {
            try {
                return clazz.getConstructor(BaseTest.class).newInstance(testClass);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Cannot create object of class: " + clazz.getName());
            }
        }
    }

    public void waitForLoad() {
        ExpectedCondition<Boolean> pageLoadCondition = d ->
                ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete");
        WebDriverWait wait = new WebDriverWait(testClass.getWebDriver(), 30);
        wait.until(pageLoadCondition);
    }

    protected List<String> getNameList(List<WebElement> elementList) {
        return new ArrayList<String>() {{
            elementList.forEach(e -> {
                testClass.waitTillElementIsVisible(e);
                add(e.getText());
            });
        }};
    }

    private void populateElementWithValidData(WebElement element, String text) {
        String value = ElementAttribute.VALUE.getAttribute();
        element.clear();
        Assert.assertEquals(element.getAttribute(value), "", "Element should be empty");

        if (!text.isEmpty()) {
            element.sendKeys(text);
            Assert.assertEquals(element.getAttribute(value), text, "Incorrect text in the element");
        }
    }

    /**
     * Enter text into element
     *
     * @param element
     * @param text
     */
    @Step("Type to the input")
    protected void typeIn(WebElement element, String text) {
        testClass.waitTillClickableAndClickElement(element);
        populateElementWithValidData(element, text);
    }

}
