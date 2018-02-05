/**
 * NoraUi is licensed under the licence GNU AFFERO GENERAL PUBLIC LICENSE
 * 
 * @author Nicolas HALLOUIN
 * @author Stéphane GRILLON
 */
package com.github.noraui.bot;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstSimpleBotWithHeadless {

    /**
     * Specific logger
     */
    private static final Logger logger = LoggerFactory.getLogger(FirstSimpleBotWithHeadless.class);

    public static void main(String[] args) throws Exception {

        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        WebDriver driver = new ChromeDriver(capabilities);
        for (int i = 0; i < 6; i++) {
            driver.get("http://www.google.com/ncr");
            WebElement element = driver.findElement(By.name("q"));
            element.sendKeys("BreizhCamp 2018");
            element.submit();
            logger.info(driver.getTitle());
            WebElement r = driver.findElement(By.xpath("//*[@id='resultStats']"));
            logger.info(r.getText());
        }
        driver.quit();
    }

}