package fun.lewisdev.deluxehub.module.modules.chat;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.module.Module;
import fun.lewisdev.deluxehub.module.ModuleType;
import fun.lewisdev.deluxehub.utility.TextUtil;
import fun.lewisdev.deluxehub.utility.universal.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoBroadcast extends Module implements Runnable {

    private Map<Integer, List<String>> broadcasts;
    private int broadcastTask = 0;
    private int count = 0;
    private int size = 0;
    private int requiredPlayers = 0;

    private Sound sound = null;
    private double volume;
    private double pitch;

    public AutoBroadcast(DeluxeHubPlugin plugin) {
        super(plugin, ModuleType.ANNOUNCEMENTS);
    }

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig(ConfigType.SETTINGS);

        broadcasts = new HashMap<>();
        int count = 0;
        for (String key : config.getConfigurationSection("announcements.announcements").getKeys(false)) {
            broadcasts.put(count, config.getStringList("announcements.announcements." + key));
            count++;
        }

        if (config.getBoolean("announcements.sound.enabled")) {
            sound = XSound.matchXSound(config.getString("announcements.sound.value")).get().parseSound();
            volume = config.getDouble("announcements.sound.volume");
            pitch = config.getDouble("announcements.sound.pitch");
        }

        requiredPlayers = config.getInt("announcements.required_players", 0);

        size = broadcasts.size();
        if (size > 0)
            broadcastTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPlugin(), this, 60L, config.getInt("announcements.delay") * 20);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(broadcastTask);
    }

    @Override
    public void run() {
        if (count == size) count = 0;

        if (count < size && Bukkit.getOnlinePlayers().size() >= requiredPlayers) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (inDisabledWorld(player.getLocation())) continue;

                broadcasts.get(count).forEach(message -> {
                    if (message.contains("<center>") && message.contains("</center>"))
                        message = TextUtil.getCenteredMessage(message);
                    player.sendMessage(TextUtil.color(message));
                });

                if (sound != null) player.playSound(player.getLocation(), sound, (float) volume, (float) pitch);
            }
            count++;
        }

    }
}
