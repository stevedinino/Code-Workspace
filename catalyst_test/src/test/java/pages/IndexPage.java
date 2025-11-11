package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class IndexPage {

    private WebDriver driver;

    public IndexPage(WebDriver driver) {
        this.driver = driver;
    }

    // 🔗 Header Navigation
    public WebElement logo() {
        return driver.findElement(By.xpath("//div[@class='logo']/a/img"));
    }

    public WebElement menuToggle() {
        return driver.findElement(By.xpath("//button[@class='menu-toggle']"));
    }

    public WebElement aboutLink() {
        return driver.findElement(By.xpath("//ul[@class='nav-links']/li/a[@href='about.html']"));
    }

    public WebElement servicesLink() {
        return driver.findElement(By.xpath("//ul[@class='nav-links']/li/a[@href='services.html']"));
    }

    public WebElement testimonialsLink() {
        return driver.findElement(By.xpath("//ul[@class='nav-links']/li/a[@href='testimonials.html']"));
    }

      public WebElement uploadLink() {
        return driver.findElement(By.xpath("//ul[@class='nav-links']/li/a[@href='upload.html']"));
    }

    public WebElement contactLink() {
        return driver.findElement(By.xpath("//ul[@class='nav-links']/li/a[@href='contact.html']"));
    }

    public WebElement privacyLink() {
        return driver.findElement(By.xpath("//ul[@class='nav-links']/li/a[@href='privacy.html']"));
    }

        public WebElement faqLink() {
        return driver.findElement(By.xpath("//ul[@class='nav-links']/li/a[@href='faq.html']"));
    }

    // 🧠 Main Content
    public WebElement headline() {
        return driver.findElement(By.xpath("//main[@class='content']/h1"));
    }

    public WebElement subtitle() {
        return driver.findElement(By.xpath("//main[@class='content']/p[@class='subtitle']"));
    }

    // 📋 Catalyst Section
    public WebElement catalystSectionTitle() {
        return driver.findElement(By.xpath("//main//section[1]/h2"));
    }

    public WebElement privacyPolicyLink() {
        return driver.findElement(By.xpath("//main//section[1]/ul/li/a[@href='privacy.html']"));
    }

    // 🛠 Services Section
    public WebElement servicesSectionTitle() {
        return driver.findElement(By.xpath("//main//section[2]/h2"));
    }

    public WebElement exploreServicesCTA() {
        return driver.findElement(By.xpath("//main//section[2]/p/a[@class='cta']"));
    } 

    // 🖼 Sidebar Image
    public WebElement sidebarImage() {
        return driver.findElement(By.xpath("//aside[@class='sidebar-image']/img"));
    }

    // 🧾 Footer
    public WebElement footerText() {
        return driver.findElement(By.xpath("//footer/p"));
    }
}