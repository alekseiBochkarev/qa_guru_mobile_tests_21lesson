package testCases;

import com.codeborne.selenide.Condition;
import config.BaseSetup;
import io.appium.java_client.MobileBy;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.back;

public class HomeWikiTests extends BaseSetup {
    @Test
    @Severity(value = SeverityLevel.NORMAL)
    @DisplayName("Check wiki search")
    @Owner("Aleksei.Bochkarev")
    @Tags({@Tag("HOMETASK21")})
    void homeWikiSearchTest() {
        back();
        $(MobileBy.id("org.wikipedia.alpha:id/search_container")).click();
        $(MobileBy.id("org.wikipedia.alpha:id/search_src_text")).sendKeys("Mars");
        $(MobileBy.id("org.wikipedia.alpha:id/page_list_item_title")).click();
        $(MobileBy.xpath("//android.widget.TextView[@text='Mars']"))
                .shouldHave(Condition.text("Mars"));
    }
}
