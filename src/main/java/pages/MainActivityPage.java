package pages;

import config.BaseSetup;
import io.appium.java_client.imagecomparison.OccurrenceMatchingOptions;
import io.appium.java_client.imagecomparison.OccurrenceMatchingResult;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import tools.Tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainActivityPage extends BaseSetup {

    @Step
    public void checkLogo (String checkMethod) throws IOException {
        String filePath="src/test/resources/ninja_PNG6.png";
        BufferedImage bufferedImage = ImageIO.read(new File(filePath));
        byte[] logoToCheck = Files.readAllBytes(Paths.get(filePath));
        switch (checkMethod) {
            case ("openCV"):
                takeScreenshotAndCheckLogoWithOpenCV(logoToCheck);
                break;
            case ("manual"):
                takeScreenshotAndCheckLogoWithLongManualComparing(bufferedImage);
                break;
        }
    }

    @Step
    public void takeScreenshotAndCheckLogoWithOpenCV(byte[] partialImage) throws IOException {
        //it is for attachment
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        //it is for comparing
        //byte[] screenshot = Base64.encodeBase64(driver.getScreenshotAs(OutputType.BYTES));
        //or
        //Allure.addAttachment("screenshot", "image/png", String.valueOf(scrFile));
        byte[] screenshot = Tools.read(scrFile);
        OccurrenceMatchingResult result = driver
                .findImageOccurrence(screenshot, partialImage, new OccurrenceMatchingOptions()
                        .withEnabledVisualization());

        assertTrue(
                result.getVisualization().length>0
        );
    }

    @Step
    public void takeScreenshotAndCheckLogoWithLongManualComparing(BufferedImage partialImage) throws IOException {
        //it is for attachment
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        BufferedImage bigImagebuffer = ImageIO.read(scrFile);
        Tools.findSubImage(bigImagebuffer, partialImage);
    }
}
