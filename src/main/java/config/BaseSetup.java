package config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import tools.Attachments;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Configuration.browserSize;
import static com.codeborne.selenide.Selenide.open;
import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;
import static tools.Attachments.getSessionId;

public class BaseSetup extends AndroidDriverProvider {
    private static final String RESOURCES_POSTFIX = ".properties";
    private static final String RESOURCES_PATH = "src/test/resources";
    public static final String ENV_RESOURCES_PREFIX = System.getProperty("env", String.valueOf(Env.envBrowserstack));
    private static final Map<String, String> env_vars = new HashMap<>();
    public static final Map<String, String> capabilities_vars = new HashMap<>();
    /**** MAIN VARS****/
    public static String appiumHost = null;
    public static String capabilities = null;
    public static String deviceName = null;

    /**** BROWSERSTACK GLOBAL VARS ****/
    public static String appURL = null;
    public static String osVersion = null;
    public static String buildName = null;

    private enum Env
    {
        envBrowserstack,
    }

    @BeforeAll
    public static void setUp()
    {
        try
        {
            initEnvVars();
            initSelenideVars();
            setAllureEnvironment();
        }
        catch (final Throwable t)
        {
            System.out.println("No fun...: " + t.getMessage());
            t.printStackTrace();
        }
    }

    @BeforeEach
    void startDriver(TestInfo testInfo)
    {
        changeExactCapability("name", testInfo.getDisplayName());
        open();
    }

    @AfterEach
    void resetApp()
    {
        Attachments.screenshotAs("Screen after test");
        Attachments.pageSource();
        String sessionId = getSessionId();
        driver.resetApp();
        driver.quit();
        if (capabilities_vars.get("browserstack.user") != null)
        {
            JsonPath jsonPath = Attachments.getBrowserStackResponse(sessionId);
            Attachments.attachVideo(jsonPath);
    //        Attachments.deviceLogs(jsonPath);
        }
    }

    @AfterAll
    public static void tearDownDriver()
    {
        //driver.quit();
    }

    private static void initSelenideVars()
    {
        browserSize = null;
        Configuration.browser = AndroidDriverProvider.class.getName();
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true));
    }

    private static void initEnvVars() throws IOException
    {
        appiumHost = MobileConfigHelper.appiumHost();
        appURL = MobileConfigHelper.appURL();
        capabilities = MobileConfigHelper.capabilities();
        parseCapabilitiesFiles(capabilities);
        osVersion = MobileConfigHelper.osVersion();
        deviceName = MobileConfigHelper.deviceName();
        buildName = MobileConfigHelper.buildName();
        //    parseCapabilitiesFiles(System.getProperty("capabilities", getEnvVar("capabilities", String.valueOf(Capabilities.capabilitiesLocal))));
        //    parseCapabilitiesFiles(System.getProperty(capabilities));
        if (capabilities_vars.containsKey("browserstack.user"))
        {
            changeExactCapability("build", buildName);
            changeExactCapability("app", appURL);
            changeExactCapability("os_version", osVersion);
        }
        changeExactCapability("device", deviceName);
    }

    private static void changeExactCapability(String key, String value)
    {
        if (capabilities_vars.get(key) != null)
        {
            capabilities_vars.replace(key, value);
        }
        else
        {
            capabilities_vars.put(key, value);
        }
    }

    private static void setAllureEnvironment()
    {
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .putAll(capabilities_vars)
                        .put("url", appiumHost)
                        .put("tags", System.getProperty("groups", ""))
                        .put("global environment", System.getenv().toString())
                        .build());
    }

    private static void parseCapabilitiesFiles(String capabilitiesProperties) throws IOException
    {
        getAllEnvFiles(capabilitiesProperties).forEach(path -> {
            final ResourceBundle bundle = ResourceBundle
                    .getBundle(path.getFileName().toString().replace(RESOURCES_POSTFIX, ""));

            bundle.keySet().forEach(key -> capabilities_vars.put(key, bundle.getString(key)));
        });
    }

    private static void parseEnvFiles() throws IOException
    {
        getAllEnvFiles().forEach(path -> {
            final ResourceBundle bundle = ResourceBundle
                    .getBundle(path.getFileName().toString().replace(RESOURCES_POSTFIX, ""));

            bundle.keySet().forEach(key -> env_vars.put(key, bundle.getString(key)));
        });
    }

    private static List<Path> getAllEnvFiles() throws IOException
    {
        return Files.walk(Paths.get(RESOURCES_PATH)).filter(p -> p.toString().contains(ENV_RESOURCES_PREFIX))
                .collect(Collectors.toList());
    }

    private static List<Path> getAllEnvFiles(String propertiesFileName) throws IOException
    {
        return Files.walk(Paths.get(RESOURCES_PATH)).filter(p -> p.toString().contains(propertiesFileName))
                .collect(Collectors.toList());
    }
}
