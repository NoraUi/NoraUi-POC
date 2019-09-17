
/**
 * NoraUi is licensed under the license GNU AFFERO GENERAL PUBLIC LICENSE
 *
 * @author Nicolas HALLOUIN
 * @author Stéphane GRILLON
 * @author Kaka69 (https://github.com/Kaka69)
 */
package com.github.noraui.sof;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.noraui.utils.Utilities.OperatingSystem;
import com.github.noraui.utils.Utilities.SystemArchitecture;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class AddCookieTest {
    /**
     * Specific LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AddCookieTest.class);

    /**
     * https://stackoverflow.com/questions/57959998/chromewebdriver-unable-to-set-cookie-when-i-update-chrome-to-77
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        TestServer testServer = new TestServer();
        testServer.start();

        final OperatingSystem currentOperatingSystem = OperatingSystem.getCurrentOperatingSystem();
        String pathWebdriver = String.format("src/test/resources/drivers/%s/googlechrome/%s/chromedriver%s", currentOperatingSystem.getOperatingSystemDir(),
                SystemArchitecture.getCurrentSystemArchitecture().getSystemArchitectureName(), currentOperatingSystem.getSuffixBinary());

        if (!new File(pathWebdriver).setExecutable(true)) {
            LOGGER.error("ERROR when change setExecutable on " + pathWebdriver);
        }

        System.setProperty("webdriver.chrome.driver", pathWebdriver);
        final ChromeOptions chromeOptions = new ChromeOptions();

        WebDriver driver = new ChromeDriver(chromeOptions);

        String cookieStr = "cookie";
        String cookieVal = "auth=ok,path=/";
        System.setProperty(cookieStr, cookieVal);
        final int indexValue = cookieVal.indexOf('=');
        final int indexPath = cookieVal.indexOf(",path=");
        final String cookieName = cookieVal.substring(0, indexValue);
        final String cookieValue = cookieVal.substring(indexValue + 1, indexPath);
        final String cookieDomain = new URI("http://localhost").getHost().replaceAll("self.", "");
        final String cookiePath = cookieVal.substring(indexPath + 6);
        Cookie authCookie = new Cookie.Builder(cookieName, cookieValue).domain(cookieDomain).path(cookiePath).expiresOn(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2020")).build();
        LOGGER.debug("New cookie created: {}={} on domain {}{}", cookieName, cookieValue, cookieDomain, cookiePath);
        System.out.println("A");
        driver.navigate().to("http://localhost:8000/unprotected");
        System.out.println("B");
        driver.get("http://localhost:8000");
        System.out.println("C");
        System.out.println("[" + driver.getPageSource() + "]");
        Options b = driver.manage();
        System.out.println("Domain: " + authCookie.getDomain());
        System.out.println("Name: " + authCookie.getName());
        System.out.println("Path: " + authCookie.getPath());
        System.out.println("Value: " + authCookie.getValue());
        System.out.println("Expiry: " + authCookie.getExpiry());
        Set<Cookie> c = b.getCookies();
        for (Cookie cookie2 : c) {
            System.out.println(cookie2);
        }
        b.addCookie(authCookie);
        System.out.println("D");
        testServer.stop();
        driver.quit();
    }
}

@SuppressWarnings("restriction")
class TestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestServer.class);
    private HttpServer server;

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
        } catch (final Exception e) {
            LOGGER.error("Exception thrown: " + e.getMessage());
        }
        server.createContext("/unprotected", new TestHttpHandler());

        final HttpContext protectedContext = server.createContext("/protected", new TestHttpHandler());

        protectedContext.setAuthenticator(new BasicAuthenticator("get") {

            @Override
            public boolean checkCredentials(String user, String pwd) {
                return user.equals("admin") && pwd.equals("admin");
            }

        });

        server.createContext("/cookieprotected", new CookieTestHttpHandler());

        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    public HttpServer getServer() {
        return server;
    }

    class TestHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            t.getRequestBody();
            final String response = "OK";
            t.sendResponseHeaders(200, response.length());
            final OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    class CookieTestHttpHandler implements HttpHandler {

        /**
         * Specific LOGGER
         */
        private final Logger logger = LoggerFactory.getLogger(CookieTestHttpHandler.class);

        @Override
        public void handle(HttpExchange t) throws IOException {
            final Set<Entry<String, List<String>>> headers = t.getRequestHeaders().entrySet();
            final OutputStream os = t.getResponseBody();
            for (final Entry<String, List<String>> h : headers) {
                logger.debug("key:[{}] and value:[{}]", h.getKey(), h.getValue());
                if ("Cookie".equals(h.getKey()) && h.getValue().contains("auth=ok")) {
                    t.getRequestBody();
                    final String response = "OK";
                    t.sendResponseHeaders(200, response.length());
                    os.write(response.getBytes());
                    break;
                }
            }
            os.close();
        }
    }
}
