package fun.lewisdev.deluxehub.module.modules.hotbar.items;

import de.tr7zw.nbtapi.NBTItem;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.config.Messages;
import fun.lewisdev.deluxehub.cooldown.CooldownType;
import fun.lewisdev.deluxehub.module.modules.hotbar.HotbarItem;
import fun.lewisdev.deluxehub.module.modules.hotbar.HotbarManager;
import fun.lewisdev.deluxehub.utility.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerHider extends HotbarItem {

    private int cooldown;
    private ItemStack hiddenItem;
    private List<UUID> hidden;

    public PlayerHider(HotbarManager hotbarManager, ItemStack item, int slot, String key) {
        super(hotbarManager, item, slot, key);
        hidden = new ArrayList<>();
        FileConfiguration config = getHotbarManager().getConfig(ConfigType.SETTINGS);
        NBTItem nbtItem = new NBTItem(ItemStackBuilder.getItemStack(config.getConfigurationSection("player_hider.hidden")).build());
        nbtItem.setString("hotbarItem", key);
        hiddenItem = nbtItem.getItem();
        cooldown = config.getInt("player_hider.cooldown");
    }

    @Override
    protected void onInteract(Player player) {

        if (!getHotbarManager().tryCooldown(player.getUniqueId(), CooldownType.PLAYER_HIDER, cooldown)) {
            Messages.COOLDOWN_ACTIVE.send(player, "%time%", getHotbarManager().getCooldown(player.getUniqueId(), CooldownType.PLAYER_HIDER));
            return;
        }

        if (!hidden.contains(player.getUniqueId())) {
            for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                player.hidePlayer(pl);
            }
            hidden.add(player.getUniqueId());
            Messages.PLAYER_HIDER_HIDDEN.send(player);

            player.getInventory().setItem(getSlot(), hiddenItem);
        } else {
            for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                player.showPlayer(pl);
            }
            hidden.remove(player.getUniqueId());
            Messages.PLAYER_HIDER_SHOWN.send(player);

            player.getInventory().setItem(getSlot(), getItem());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (hidden.contains(player.getUniqueId())) {
            for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                player.showPlayer(pl);
            }
        }
        hidden.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        hidden.forEach(uuid -> {
            Bukkit.getPlayer(uuid).hidePlayer(player);
        });
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (getHotbarManager().inDisabledWorld(player.getLocation()) && hidden.contains(player.getUniqueId())) {
            for (Player p : Bukkit.getOnlinePlayers()) player.showPlayer(p);
            hidden.remove(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (hidden.contains(player.getUniqueId())) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                player.showPlayer(p);
            }
            hidden.remove(player.getUniqueId());
        }
    }
}