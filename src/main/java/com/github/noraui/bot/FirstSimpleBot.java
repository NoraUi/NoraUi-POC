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
import org.openqa.selenium.remote.DesiredCapabilities;

public class FirstSimpleBot {

    public static void main(String[] args) throws Exception {

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        WebDriver driver = new ChromeDriver(capabilities);
        for (int i = 0; i < 6; i++) {
            driver.get("http://www.google.com/ncr");
            WebElement element = driver.findElement(By.name("q"));
            element.sendKeys("BreizhCamp 2018");
            element.submit();
            System.out.println(driver.getTitle());
            WebElement r = driver.findElement(By.xpath("//*[@id='resultStats']"));
            System.out.println(r.getText());
        }
        driver.quit();
    }

}