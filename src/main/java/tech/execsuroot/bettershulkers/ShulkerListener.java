package tech.execsuroot.bettershulkers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static tech.execsuroot.bettershulkers.MainConfig.mainConfig;

public class ShulkerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractWithShulkerBoxInHand(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
        Player player = event.getPlayer();
        ItemStack itemStack = getItemInActiveHand(player);
        boolean isShulker = itemStack.getType().name().endsWith("SHULKER_BOX");
        if (!isShulker) return;
        MainConfig.PermissionSection permissionSection = mainConfig.getPermission();
        if (permissionSection.isEnabled() && !player.hasPermission(permissionSection.getPermission())) return;
        ShulkerInventory.open(itemStack, player);
    }

    private ItemStack getItemInActiveHand(Player player) {
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();
        return mainHandItem.getType().isAir() ? offHandItem : mainHandItem;
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, BetterShulkersPlugin.getInstance());
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
