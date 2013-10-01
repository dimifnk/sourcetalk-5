package sourcetalk.continuousdelivery;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IntegrationTest {
    @BeforeClass
    public static void setUp() {
        Main.start();
    }

    @AfterClass
    public static void tearDown() {
        Main.stop();
    }

    @Test
    public void addFriend() throws Exception {
        WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_9);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        HtmlPage page = webClient.getPage("http://localhost:" + Environment.getConfiguration().getPortHttp());
        assertTrue(page.asText().contains("Welcome to SourceTalk!"));

        ((HtmlTextInput) page.getElementByName("user")).setValueAttribute("Otto");
        page = ((HtmlSubmitInput) page.getElementByName("start")).click();

        assertTrue(page.asText().contains("Hello Otto!"));
        assertTrue(page.asText().contains("'unittest' environment"));
        assertTrue(page.asText().contains("Homer"));
        assertFalse(page.asText().contains("Bart"));

        ((HtmlTextInput) page.getElementByName("firstName")).setValueAttribute("Bart");
        ((HtmlTextInput) page.getElementByName("lastName")).setValueAttribute("Simpson");
        page = ((HtmlSubmitInput) page.getElementByName("submit")).click();

        assertTrue(page.asText().contains("Hello Otto!"));
        assertTrue(page.asText().contains("Homer"));
        assertTrue(page.asText().contains("Bart"));

        webClient.closeAllWindows();
    }
}
