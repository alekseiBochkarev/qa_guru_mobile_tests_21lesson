package config;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.codeborne.selenide.WebDriverProvider;

public class AndroidDriverProvider implements WebDriverProvider {
    public static AppiumDriver driver;

    @Override
    @CheckReturnValue
    @Nonnull
    public WebDriver createDriver(DesiredCapabilities capabilities)
    {
        for (Map.Entry<String, String> entry : BaseSetup.capabilities_vars.entrySet())
        {
            capabilities.setCapability(entry.getKey(), entry.getValue());
        }
        return driver = new AndroidDriver(getBaseSetupUrl(), capabilities);
    }

    public static URL getBaseSetupUrl()
    {
        try
        {
            return new URL(BaseSetup.appiumHost);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
