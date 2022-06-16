package testCases;

import com.codeborne.selenide.CollectionCondition;
import config.BaseSetup;
import io.appium.java_client.MobileBy;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class SearchTests extends BaseSetup {
    @Test
    void searchTest() {
        back();
        step("Type search", () -> {
            $(MobileBy.id("org.wikipedia.alpha:id/search_container")).click();
            $(MobileBy.id("org.wikipedia.alpha:id/search_src_text")).sendKeys("BrowserStack");
        });
        step("Verify content found", () ->
                $$(MobileBy.id("org.wikipedia.alpha:id/page_list_item_title"))
                        .shouldHave(CollectionCondition.sizeGreaterThan(0)));
    }
}
