package tech.execsuroot.bettershulkers;

import net.kyori.adventure.sound.Sound;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.block.CraftShulkerBox;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

import static tech.execsuroot.bettershulkers.MainConfig.mainConfig;

public class ShulkerInventory implements Listener {

    private final Player viewer;
    private final ItemStack itemStack;
    private final BlockStateMeta itemMeta;
    private final CraftShulkerBox itemState;
    private final InventoryHolder holder;
    private final Inventory inventory;

    public static void open(ItemStack itemStack, Player viewer) {
        boolean isItemShulker = itemStack.getType().name().endsWith("SHULKER_BOX");
        if (!isItemShulker) {
            throw new IllegalArgumentException("Expected an item stack of shulker box type, but received " + itemStack.getType() + " instead.");
        }
        new ShulkerInventory(itemStack, viewer).open();
    }

    private ShulkerInventory(ItemStack itemStack, Player viewer) {
        this.viewer = viewer;
        this.itemStack = itemStack;
        this.itemMeta = (BlockStateMeta) itemStack.getItemMeta();
        this.itemState = (CraftShulkerBox) itemMeta.getBlockState();
        this.holder = createInventoryHolder();
        this.inventory = createInventory();
        loadContents();
    }

    private void open() {
        register();
        playSound(mainConfig.getOpenSound());
        viewer.openInventory(inventory);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() != holder) return;
        playSound(mainConfig.getCloseSound());
        saveContents();
        unregister();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onShulkerMove(InventoryClickEvent event) {
        InventoryView view = event.getWhoClicked().getOpenInventory();
        if (view.getTopInventory().getHolder() != holder) return;
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;
        if (event.getCurrentItem().getType().name().endsWith("SHULKER_BOX")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onShulkerDrop(PlayerDropItemEvent event) {
        if (!event.getPlayer().equals(viewer)) return;
        ItemStack droppedItem = event.getItemDrop().getItemStack();
        if (droppedItem.getType().name().endsWith("SHULKER_BOX")) {
            event.setCancelled(true);
        }
    }

    private void playSound(MainConfig.SoundSection soundSection) {
        if (soundSection.isEnabled()) {
            viewer.playSound(Sound.sound().type(soundSection.getSound()).volume(soundSection.getVolume()).build());
        }
    }

    private InventoryHolder createInventoryHolder() {
        return new InventoryHolder() {
            @Override
            public @NotNull Inventory getInventory() {
                return inventory;
            }
        };
    }

    private Inventory createInventory() {
        Inventory inventory;
        if (itemMeta.hasDisplayName()) {
            inventory = Bukkit.createInventory(holder, InventoryType.SHULKER_BOX, itemMeta.displayName());
        } else {
            inventory = Bukkit.createInventory(holder, InventoryType.SHULKER_BOX);
        }
        return inventory;
    }

    private void loadContents() {
        ItemStack[] contents = itemState.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            inventory.setItem(i, contents[i]);
        }
    }

    private void saveContents() {
        ShulkerBoxBlockEntity entity = itemState.getTileEntity();
        ItemStack[] contents = inventory.getContents();
        for (int i = 0; i < contents.length; i++) {
            entity.setItem(i, getItemStackHandle(contents[i]));
        }
        RegistryAccess registryAccess = MinecraftServer.getServer().registryAccess();
        entity.saveToItem(getItemStackHandle(itemStack), registryAccess);
    }

    private net.minecraft.world.item.ItemStack getItemStackHandle(ItemStack itemStack) {
        if (itemStack == null) return net.minecraft.world.item.ItemStack.fromBukkitCopy(ItemStack.of(Material.AIR));
        return ((CraftItemStack) itemStack).handle;
    }

    private void register() {
        Bukkit.getPluginManager().registerEvents(this, BetterShulkersPlugin.getInstance());
    }

    private void unregister() {
        HandlerList.unregisterAll(this);
    }
}
