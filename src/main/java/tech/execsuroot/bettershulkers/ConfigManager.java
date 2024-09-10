package tech.execsuroot.bettershulkers;

import de.exlll.configlib.NameFormatters;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;

import java.nio.file.Path;

public class ConfigManager {

    public static final ConfigManager configManager = new ConfigManager();

    private final Path configFolder = BetterShulkersPlugin.getInstance().getDataPath();
    private final YamlConfigurationProperties yamlProperties = YamlConfigurationProperties.newBuilder()
            .setNameFormatter(NameFormatters.IDENTITY)
            .build();

    public <T> T loadConfig(Class<T> configClass, String fileName) {
        return YamlConfigurations.update(configFolder.resolve(fileName), configClass, yamlProperties);
    }
}
