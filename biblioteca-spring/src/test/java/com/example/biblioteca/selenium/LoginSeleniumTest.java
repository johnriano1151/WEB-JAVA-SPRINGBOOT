package com.example.biblioteca.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // Para ejecutar sin interfaz gráfica
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testLoginPageLoads() {
        driver.get("http://localhost:" + port + "/login");
        
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h3")));
        assertEquals("Biblioteca Digital", title.getText());
        
        WebElement usernameInput = driver.findElement(By.id("username"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        
        assertTrue(usernameInput.isDisplayed());
        assertTrue(passwordInput.isDisplayed());
        assertTrue(loginButton.isDisplayed());
    }

    @Test
    void testSuccessfulLogin() {
        driver.get("http://localhost:" + port + "/login");
        
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        WebElement passwordInput = driver.findElement(By.id("password"));
        
        usernameInput.sendKeys("admin");
        passwordInput.sendKeys("12345");
        
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();
        
        // Esperar a que se redirija a la página principal
        WebElement welcomeMessage = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".navbar-text")));
            
        assertTrue(welcomeMessage.getText().contains("Bienvenido"));
    }

    @Test
    void testFailedLogin() {
        driver.get("http://localhost:" + port + "/login");
        
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        WebElement passwordInput = driver.findElement(By.id("password"));
        
        usernameInput.sendKeys("usuario_incorrecto");
        passwordInput.sendKeys("clave_incorrecta");
        
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();
        
        // Esperar a que aparezca el mensaje de error
        WebElement errorMessage = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-danger")));
            
        assertTrue(errorMessage.getText().contains("incorrectos"));
    }
}