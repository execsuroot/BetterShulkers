package tech.execsuroot.bettershulkers;

import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterShulkersPlugin extends JavaPlugin {

    @Getter
    private static BetterShulkersPlugin instance;
    private final ShulkerListener shulkerListener = new ShulkerListener();

    public BetterShulkersPlugin() {
        super();
        instance = this;
    }

    @Override
    public void onEnable() {
        shulkerListener.register();
        getComponentLogger().info(MiniMessage.miniMessage().deserialize(
                "<green>BetterShulkers v" + getVersion() + " enabled."
        ));
    }

    @Override
    public void onDisable() {
        shulkerListener.unregister();
        getComponentLogger().info(MiniMessage.miniMessage().deserialize(
                "<red>BetterShulkers v" + getVersion() + " disabled."
        ));
    }

    @SuppressWarnings("UnstableApiUsage")
    private String getVersion() {
        return getPluginMeta().getVersion();
    }
}
