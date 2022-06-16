package config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

import static config.BaseSetup.ENV_RESOURCES_PREFIX;

public class MobileConfigHelper {

    private static MobileConfig getConfig()
    {
        if (System.getProperty("env") == null)
        {
            System.setProperty("env", ENV_RESOURCES_PREFIX);
        }
        return ConfigFactory.newInstance().create(MobileConfig.class, System.getProperties());
    }

    private static CapabilitiesBrowserStackConfig getCapabilitiesBrowserstackConfig()
    {
        return ConfigFactory.newInstance().create(CapabilitiesBrowserStackConfig.class, System.getProperties());
    }

    @Config.Key("capabilities")
    public static String capabilities()
    {
        return getConfig().capabilities();
    }

    @Config.Key("url")
    public static String appiumHost()
    {
        return getConfig().appiumHost();
    }

    @Config.Key("deviceName")
    public static String deviceName()
    {
        return getCapabilitiesBrowserstackConfig().deviceName();
    }

    @Config.Key("browserstack.user")
    public static String browserStackUserName()
    {
        return getCapabilitiesBrowserstackConfig().browserStackUserName();
    }

    @Config.Key("browserstack.key")
    public static String browserStackKey()
    {
        return getCapabilitiesBrowserstackConfig().browserStackKey();
    }
}
