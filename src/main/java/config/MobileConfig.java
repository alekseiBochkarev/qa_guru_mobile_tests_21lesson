package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:envBrowserstack.properties" })
public interface MobileConfig extends Config {
    @Key("url")
    String appiumHost();

    @Key("capabilities")
    String capabilities();
}
