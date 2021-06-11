package fun.lewisdev.deluxehub.module.modules.player;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.module.Module;
import fun.lewisdev.deluxehub.module.ModuleType;
import fun.lewisdev.deluxehub.utility.PlaceholderUtil;
import fun.lewisdev.deluxehub.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener extends Module {

    private boolean joinQuitMessagesEnabled;
    private String joinMessage;
    private String quitMessage;

    private List<String> joinActions;

    private boolean spawnHeal;
    private boolean extinguish;
    private boolean clearInventory;

    private boolean fireworkEnabled;
    private boolean fireworkFirstJoin;
    private boolean fireworkFlicker;
    private boolean fireworkTrail;
    private int fireworkPower;
    private String fireworkType;
    private List<Color> fireworkColors;

    public PlayerListener(DeluxeHubPlugin plugin) {
        super(plugin, ModuleType.PLAYER_LISTENER);
    }

    @Override
    public void onEnable() {

        // Load config stuff
        FileConfiguration config = getConfig(ConfigType.SETTINGS);
        joinQuitMessagesEnabled = config.getBoolean("join_leave_messages.enabled");
        joinMessage = config.getString("join_leave_messages.join_message");
        quitMessage = config.getString("join_leave_messages.quit_message");

        joinActions = config.getStringList("join_events");

        spawnHeal = config.getBoolean("join_settings.heal", false);
        extinguish = config.getBoolean("join_settings.extinguish", false);
        clearInventory = config.getBoolean("join_settings.clear_inventory", false);

        fireworkEnabled = config.getBoolean("join_settings.firework.enabled", true);
        if (fireworkEnabled) {
            fireworkFirstJoin = config.getBoolean("join_settings.firework.first_join_only", true);
            fireworkType = config.getString("join_settings.firework.type", "BALL_LARGE");
            fireworkPower = config.getInt("join_settings.firework.power", 1);
            fireworkFlicker = config.getBoolean("join_settings.firework.flicker", true);
            fireworkTrail = config.getBoolean("join_settings.firework.power", true);

            fireworkColors = new ArrayList<>();
            config.getStringList("join_settings.firework.colors").forEach(c -> {
                Color color = TextUtil.getColor(c);
                if (color != null) fireworkColors.add(color);
            });
        }
    }

    @Override
    public void onDisable() {

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (inDisabledWorld(player.getLocation())) return;

        // Join message handling
        if (joinQuitMessagesEnabled) {
            if (joinMessage.equals("")) event.setJoinMessage(null);
            else {
                String message = PlaceholderUtil.setPlaceholders(joinMessage, player);
                event.setJoinMessage(TextUtil.color(message));
            }
        }

        // Heal the player
        if (spawnHeal) {
            player.setFoodLevel(20);
            player.setHealth(player.getMaxHealth());
        }

        // Extinguish
        if (extinguish) player.setFireTicks(0);

        // Clear the player inventory
        if (clearInventory) player.getInventory().clear();

        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> {
            // Join events
            executeActions(player, joinActions);

            // Firework
            if (fireworkEnabled) {
                if (fireworkFirstJoin) {
                    if (!player.hasPlayedBefore()) spawnFirework(player);
                } else {
                    spawnFirework(player);
                }
            }
        }, 3L);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        if (inDisabledWorld(player.getLocation())) return;

        if (joinQuitMessagesEnabled) {
            if (quitMessage.equals("")) event.setQuitMessage(null);
            else {
                String message = PlaceholderUtil.setPlaceholders(quitMessage, player);
                event.setQuitMessage(TextUtil.color(message));
            }
        }

        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (inDisabledWorld(player.getLocation()))
            player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
    }

    public void spawnFirework(Player player) {
        Firework f = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                .flicker(fireworkFlicker)
                .trail(fireworkTrail)
                .with(FireworkEffect.Type.valueOf(fireworkType))
                .withColor(fireworkColors).build());
        fm.setPower(fireworkPower);
        f.setFireworkMeta(fm);

        //Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> f.remove(), 100L);
    }
}
