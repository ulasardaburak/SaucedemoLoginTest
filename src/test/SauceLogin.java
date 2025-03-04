import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class SauceLogin {
    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "src/driver/chrome-driver/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Test edilecek kullanıcı bilgileri
        List<String> validUsers = Arrays.asList("standard_user");
        String password = "secret_sauce";

        // Başarılı giriş yapacak kullanıcılar
        for (String user : validUsers) {
            testLogin(driver, user, password, true);
        }

        // Hatalı giriş yapılacak kullanıcılar
        testLogin(driver, "locked_out_user", password, false);
        testLogin(driver, "invalid_user", "wrong_password", false);

        driver.quit();
    }

    public static void testLogin(WebDriver driver, String username, String password, boolean expectedSuccess) {
        driver.get("https://www.saucedemo.com/");

        // Kullanıcı adı ve şifre girişi
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        usernameField.clear();
        passwordField.clear();

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();

        if (expectedSuccess) {
            boolean isLoggedIn = driver.findElements(By.className("inventory_list")).size() > 0;
            if (isLoggedIn) {
                System.out.println(username + " ile giriş başarılı.");
            } else {
                System.out.println(username + " ile giriş başarısız!");
            }
        } else {
            WebElement errorMessage = driver.findElement(By.cssSelector("[data-test='error']"));
            if (errorMessage.isDisplayed()) {
                System.out.println(username + " için hata mesajı: " + errorMessage.getText());
            } else {
                System.out.println(username + " için hata mesajı bulunamadı!");
            }
        }
    }
}
