package fun.lewisdev.deluxehub.module.modules.visual.scoreboard;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.module.Module;
import fun.lewisdev.deluxehub.module.ModuleType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;

public class ScoreboardManager extends Module {

    private int scoreTask;
    private Map<UUID, ScoreHelper> players;

    private long joinDelay;
    private long worldDelay;
    private String title;
    private List<String> lines;

    public ScoreboardManager(DeluxeHubPlugin plugin) {
        super(plugin, ModuleType.SCOREBOARD);
    }

    @Override
    public void onEnable() {
        players = new HashMap<>();
        FileConfiguration config = getConfig(ConfigType.SETTINGS);

        title = config.getString("scoreboard.title");
        lines = config.getStringList("scoreboard.lines");

        joinDelay = config.getLong("scoreboard.display_delay.server_enter", 0L);
        worldDelay = config.getLong("scoreboard.display_delay.world_change", 0L);

        if (config.getBoolean("scoreboard.refresh.enabled")) {
            scoreTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPlugin(), new ScoreUpdateTask(this), 0L, config.getLong("scoreboard.refresh.rate"));
        }

        getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> Bukkit.getOnlinePlayers().stream().filter(player -> !inDisabledWorld(player.getLocation())).forEach(this::createScoreboard), 20L);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(scoreTask);
        Bukkit.getOnlinePlayers().forEach(this::removeScoreboard);
    }

    public void createScoreboard(Player player) {
        players.put(player.getUniqueId(), updateScoreboard(player.getUniqueId()));
    }

    public ScoreHelper updateScoreboard(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return null;

        int lines = this.lines.size();

        ScoreHelper helper = players.get(player.getUniqueId());
        if (helper == null) helper = new ScoreHelper(player);
        helper.setTitle(title);

        for (String text : this.lines) {
            helper.setSlot(lines, text);
            lines--;
        }

        return helper;

    }

    public void removeScoreboard(Player player) {
        if (players.containsKey(player.getUniqueId())) {
            players.remove(player.getUniqueId());
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

    public boolean hasScore(UUID uuid) {
        return players.containsKey(uuid);
    }

    public Collection<UUID> getPlayers() {
        return players.keySet();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!inDisabledWorld(player.getLocation()) && !hasScore(player.getUniqueId())) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> createScoreboard(player), joinDelay);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removeScoreboard(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldChange(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getWorld().getName().equals(event.getTo().getWorld().getName())) return;

        if (inDisabledWorld(event.getTo().getWorld()) && players.containsKey(player.getUniqueId())) {
            removeScoreboard(player);
        } else if (!players.containsKey(player.getUniqueId())) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> createScoreboard(player), worldDelay);
        }
    }

}
