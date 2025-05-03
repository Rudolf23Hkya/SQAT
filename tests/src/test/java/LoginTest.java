package com.example.tests;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.*;
import java.net.URL;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final By usernameLocator = By.id("username");
    private final By passwordLocator = By.id("password");
    private final By loginButtonLocator = By.cssSelector("button[type='submit']");
    private final By flashMessageLocator = By.id("flash");
    private final By logoutButtonLocator = By.cssSelector("a.button");

    @Before
    public void setup() throws Exception {
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private WebElement waitAndReturnElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @Test
    public void testSuccessfulLoginAndLogout() {
        driver.get("http://the-internet.herokuapp.com/login");
        // Login with valid credentials
        waitAndReturnElement(usernameLocator).sendKeys("tomsmith");
        waitAndReturnElement(passwordLocator).sendKeys("SuperSecretPassword!");
        waitAndReturnElement(loginButtonLocator).click();

        // Verify login success
        String successMsg = waitAndReturnElement(flashMessageLocator).getText();
        Assert.assertTrue(successMsg.contains("You logged into a secure area!"));

        // Logout
        waitAndReturnElement(logoutButtonLocator).click();
        String logoutMsg = waitAndReturnElement(flashMessageLocator).getText();
        Assert.assertTrue(logoutMsg.contains("You logged out of the secure area!"));
    }

    @Test
    public void testInvalidLogin() {
        driver.get("http://the-internet.herokuapp.com/login");
        // Login with invalid credentials
        waitAndReturnElement(usernameLocator).sendKeys("invalidUser");
        waitAndReturnElement(passwordLocator).sendKeys("invalidPass");
        waitAndReturnElement(loginButtonLocator).click();

        // Verify login failure
        String errorMsg = waitAndReturnElement(flashMessageLocator).getText();
        Assert.assertTrue(errorMsg.contains("Your username is invalid!"));
    }
}
