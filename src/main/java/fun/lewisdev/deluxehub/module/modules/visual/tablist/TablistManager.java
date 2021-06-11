package fun.lewisdev.deluxehub.module.modules.visual.tablist;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.module.Module;
import fun.lewisdev.deluxehub.module.ModuleType;
import fun.lewisdev.deluxehub.utility.PlaceholderUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TablistManager extends Module {

    private List<UUID> players;
    private int tablistTask;

    private String header, footer;

    public TablistManager(DeluxeHubPlugin plugin) {
        super(plugin, ModuleType.TABLIST);
    }

    @Override
    public void onEnable() {
        players = new ArrayList<>();

        FileConfiguration config = getConfig(ConfigType.SETTINGS);

        header = config.getStringList("tablist.header").stream().collect(Collectors.joining("\n"));
        footer = config.getStringList("tablist.footer").stream().collect(Collectors.joining("\n"));

        if (config.getBoolean("tablist.refresh.enabled")) {
            tablistTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPlugin(), new TablistUpdateTask(this), 0L, config.getLong("tablist.refresh.rate"));
        }

        getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), () ->
                Bukkit.getOnlinePlayers().stream().filter(player -> !inDisabledWorld(player.getLocation())).forEach(this::createTablist), 20L);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(tablistTask);
        Bukkit.getOnlinePlayers().forEach(this::removeTablist);
    }

    public void createTablist(Player player) {
        UUID uuid = player.getUniqueId();
        players.add(uuid);
        updateTablist(uuid);
    }

    public boolean updateTablist(UUID uuid) {
        if (!players.contains(uuid)) return false;

        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;

        TablistHelper.sendTabList(player, PlaceholderUtil.setPlaceholders(header, player), PlaceholderUtil.setPlaceholders(footer, player));
        return true;
    }

    public void removeTablist(Player player) {
        if (players.contains(player.getUniqueId())) {
            players.remove(player.getUniqueId());
            TablistHelper.sendTabList(player, null, null);
        }
    }

    public List<UUID> getPlayers() {
        return players;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!inDisabledWorld(player.getLocation())) createTablist(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        removeTablist(event.getPlayer());
    }

    @EventHandler
    public void onWorldChange(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getWorld().getName().equals(event.getTo().getWorld().getName())) return;

        if (inDisabledWorld(event.getTo().getWorld()) && players.contains(player.getUniqueId())) removeTablist(player);
        else createTablist(player);
    }

}
