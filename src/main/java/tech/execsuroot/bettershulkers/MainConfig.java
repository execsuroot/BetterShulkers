package tech.execsuroot.bettershulkers;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Sound;

import static tech.execsuroot.bettershulkers.ConfigManager.configManager;

@Configuration
@Data
public class MainConfig {

    public static final MainConfig mainConfig = configManager.loadConfig(MainConfig.class, "config.yml");

    @Comment("Permission required to open shulker boxes in hands.")
    private PermissionSection permission = new PermissionSection(false, "bettershulkers.open");
    @Comment("Sound played when a shulker box is opened in hands.")
    private SoundSection openSound = new SoundSection(true, Sound.BLOCK_SHULKER_BOX_OPEN, 1f);
    @Comment("Sound played when a shulker box is closed in hands.")
    private SoundSection closeSound = new SoundSection(true, Sound.BLOCK_SHULKER_BOX_CLOSE, 1f);

    @Configuration
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PermissionSection {
        @Comment("Set to false to disable the permission check.")
        private boolean enabled;
        @Comment("The permission to check for.")
        private String permission;
    }

    @Configuration
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SoundSection {
        @Comment("Set to false to disable the sound.")
        private boolean enabled;
        @Comment("The sound played.")
        private Sound sound;
        @Comment("The volume of the sound.")
        private float volume;
    }
}
