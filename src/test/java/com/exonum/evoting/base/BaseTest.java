package com.exonum.evoting.base;

import com.exonum.evoting.enums.LocatorType;
import com.exonum.evoting.pages.MainPage;
import io.restassured.response.Response;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.reporters.SuiteHTMLReporter;
import org.testng.reporters.XMLReporter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

@Log
@Listeners({SuiteHTMLReporter.class, XMLReporter.class})
public class BaseTest {

    private static Actions actions = null;
    private static WebDriver driver = null;
    private static Wait wait = null;

    private static final String MAIL_SERVICE_URL = "https://api.guerrillamail.com/ajax.php";
    private static final String BASE_URL = "https://exonum.com/demo/voting/";

    protected static final int EXPLICIT = 8;

    // Anyway BaseTest can be created only by page classes
    public WebDriver getWebDriver() {
        return driver;
    }

    @BeforeTest
    protected void setUp() {
        configureDriver();
        wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        turnOnImplicitWaits();
        actions = new Actions(driver);
    }

    @AfterMethod
    protected void afterMethod(ITestResult testResult) {
        if (testResult.getStatus() == ITestResult.FAILURE) snapScreenshot();
    }

    @AfterTest
    protected void tearDown() {
        driver.quit();
    }

    private static void setDriverByOs() {
        String osDriverName;
        String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if ((os.contains("mac")) || (os.contains("darwin"))) {
            osDriverName = "mac64";
        } else if (os.contains("win")) {
            osDriverName = "win_32.exe";
        } else if (os.contains("nux")) {
            osDriverName = "linux64";
        } else {
            throw new RuntimeException("Cannot define your OS: " + os);
        }
        System.setProperty("webdriver.chrome.driver", "src/test/resources/driver/chrome_" + osDriverName);
    }

    private void configureDriver() {
        setDriverByOs();
        ChromeOptions chromeOptions = new ChromeOptions().addArguments("disable-infobars", "--disable-extensions", "test-type");
        chromeOptions.setCapability("pageLoadStrategy", "none");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().setSize(new Dimension(1440, 900));
    }

    public WebElement findElement(LocatorType locatorType, String locatorStr) {
        WebElement element = null;
        try {
            turnOffImplicitWaits();
            WebDriverWait wait = new WebDriverWait(driver, EXPLICIT);
            element = wait.ignoring(StaleElementReferenceException.class).until(
                    ExpectedConditions.visibilityOfElementLocated(LocatorType.getBy(locatorType, locatorStr)));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Assert.fail(String.format("Element with xpath: <%s> not found", locatorStr));
        } catch (TimeoutException e) {
            e.printStackTrace();
            Assert.fail(String.format("Timeout exception caught while waiting for element: <%s>", locatorStr));
        } finally {
            turnOnImplicitWaits();
        }

        return element;
    }

    public MainPage openEvoting() {
        driver.get(BASE_URL);
        return new MainPage(this);
    }

    private void snapScreenshot() {
        DateFormat date = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String fileName = this.getClass().getName() + "_" + date.format(new Date());
        String screenshotPath = System.getProperties().get("user.dir") + "\\screenshots\\";
        try {
            File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(f, new File(screenshotPath + File.separator + fileName + ".png"));
            log.config("Screenshot file [" + fileName + "]" + " written in [" + screenshotPath + "]");
        } catch (IOException e) {
            throw new RuntimeException("Unable to store screenshot.", e);
        }
    }

    private void turnOffImplicitWaits() {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }

    private void turnOnImplicitWaits() {
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    public void waitTillClickableAndClickElement(WebElement element) {
        waitTillElementIsClickable(element);
        turnOffImplicitWaits();
        element.click();
        turnOnImplicitWaits();
    }

    public void waitTillElementIsClickable(WebElement element) {
        try {
            turnOffImplicitWaits();
            WebDriverWait wait = new WebDriverWait(driver, EXPLICIT);
            wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(element));
        } catch (NoSuchElementException e) {
            Assert.fail("NoSuchElement exception caught for element " + e.toString());
        } catch (TimeoutException e) {
            Assert.fail("Timeout exception caught for element " + e.toString());
        } finally {
            turnOnImplicitWaits();
        }

        actions.moveToElement(element).build().perform();
    }

    public void waitTillElementIsVisible(WebElement element) {
        waitTillElementIsVisible(element, false, EXPLICIT);
    }

    public void waitTillElementIsVisible(WebElement element, boolean throwException, long timeOutInSeconds) {
        try {
            turnOffImplicitWaits();
            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(element));
        } catch (NoSuchElementException e) {
            if (throwException) {
                throw (e);
            }
            Assert.fail("NoSuchElement exception caught for element " + e.toString());
        } catch (TimeoutException e) {
            if (throwException) {
                throw (e);
            }
            Assert.fail("Timeout exception caught for element " + e.toString());
        } finally {
            turnOnImplicitWaits();
        }
    }

    public void waitTillElementsAreVisible(List<WebElement> elements) {
        waitTillElementsAreVisible(elements, false, EXPLICIT);
    }

    public void waitTillElementsAreVisible(List<WebElement> elements, boolean throwException, long timeOutInSeconds) {
        try {
            turnOffImplicitWaits();
            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOfAllElements(elements));
        } catch (NoSuchElementException e) {
            if (throwException) {
                throw (e);
            }
            Assert.fail("NoSuchElement exception caught for element " + e.toString());
        } catch (TimeoutException e) {
            if (throwException) {
                throw (e);
            }
            Assert.fail("Timeout exception caught for element " + e.toString());
        } finally {
            turnOnImplicitWaits();
        }
    }

    public void scrollToElementByJs(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToElementByJsAndClick(WebElement element) {
        scrollToElementByJs(element);
        waitTillClickableAndClickElement(element);
    }

    public static Map<String, String> getTempEmail() {
        Map<String, String> mailData = new HashMap<>();
        Response response = given().log().all().relaxedHTTPSValidation().
                queryParam("f", "get_email_address").
                queryParam("lang", "en").
                get(MAIL_SERVICE_URL);
        mailData.put("mail", response.jsonPath().get("email_addr"));
        mailData.put("token", response.jsonPath().getString("sid_token"));
        return mailData;
    }

    public static int getNeededEmailId(String token, int letterIndex) {
        String[] currentTokens = new String[1];
        currentTokens[0] = token;
        final int[] id = {0};
        wait.until((w) -> {
            Response response = given().log().all().relaxedHTTPSValidation().
                    queryParam("f", "check_email").
                    queryParam("lang", "en").
                    queryParam("seq", "0").
                    queryParam("sid_token", currentTokens[0]).
                    get(MAIL_SERVICE_URL);
            String tmpToken = response.jsonPath().getString("sid_token");
            if (!tmpToken.equals(currentTokens[0])) currentTokens[0] = tmpToken;
            try {
                id[0] = response.jsonPath().getInt("list.mail_id[" + letterIndex + "]");
            } catch (Exception ignored) {}
            return id[0] > 10;
        });
        return id[0];
    }

    public String getMailBody(String token, int msgId) {
        Response response = given().relaxedHTTPSValidation().
                queryParam("f", "fetch_email").
                queryParam("lang", "en").
                queryParam("sid_token", token).
                queryParam("email_id", msgId).
                get(MAIL_SERVICE_URL);
        return response.jsonPath().getString("mail_body");
    }

}
