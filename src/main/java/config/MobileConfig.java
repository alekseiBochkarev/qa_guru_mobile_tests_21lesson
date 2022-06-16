package config;

import org.aeonbits.owner.Config;

public interface MobileConfig extends Config {
    @Key("url")
    String appiumHost();

    @Key("capabilities")
    String capabilities();
}
