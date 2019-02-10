package com.exonum.evoting.enums;

import org.openqa.selenium.By;

public enum LocatorType {
    CLASS_NAME,
    CSS_SELECTOR,
    ID,
    LINK_TEXT,
    NAME,
    PARTIAL_LINK_TEXT,
    TAG_NAME,
    XPATH;

    public static By getBy(LocatorType type, String locatorStr) {
        By byLocator;
        switch (type) {
            case CLASS_NAME:
                byLocator = By.className(locatorStr);
                break;
            case CSS_SELECTOR:
                byLocator = By.cssSelector(locatorStr);
                break;
            case ID:
                byLocator = By.id(locatorStr);
                break;
            case LINK_TEXT:
                byLocator = By.linkText(locatorStr);
                break;
            case NAME:
                byLocator = By.name(locatorStr);
                break;
            case PARTIAL_LINK_TEXT:
                byLocator = By.partialLinkText(locatorStr);
                break;
            case TAG_NAME:
                byLocator = By.tagName(locatorStr);
                break;
            case XPATH:
                byLocator = By.xpath(locatorStr);
                break;
            default:
                throw new RuntimeException("Undefined locator type: " + type);
        }
        return byLocator;
    }
}
