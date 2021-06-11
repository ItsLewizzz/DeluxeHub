package fun.lewisdev.deluxehub.module.modules.world;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.cooldown.CooldownType;
import fun.lewisdev.deluxehub.module.Module;
import fun.lewisdev.deluxehub.module.ModuleType;
import fun.lewisdev.deluxehub.utility.universal.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class Launchpad extends Module {

    private double launch;
    private double launchY;
    private List<String> actions;
    private Material topBlock;
    private Material bottomBlock;

    public Launchpad(DeluxeHubPlugin plugin) {
        super(plugin, ModuleType.LAUNCHPAD);
    }

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig(ConfigType.SETTINGS);
        launch = config.getDouble("launchpad.launch_power", 1.3);
        launchY = config.getDouble("launchpad.launch_power_y", 1.2);
        actions = config.getStringList("launchpad.actions");
        topBlock = XMaterial.matchXMaterial(config.getString("launchpad.top_block")).get().parseMaterial();
        bottomBlock = XMaterial.matchXMaterial(config.getString("launchpad.bottom_block")).get().parseMaterial();

        if (launch > 4.0) launch = 4.0;
        if (launchY > 4.0) launchY = 4.0;
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        if (inDisabledWorld(location)) return;

        // Check for launchpad block
        if (location.getBlock().getType() == topBlock && location.subtract(0, 1, 0).getBlock().getType() == bottomBlock) {

            // Check for cooldown
            if (tryCooldown(player.getUniqueId(), CooldownType.LAUNCHPAD, 1)) {
                player.setVelocity(location.getDirection().multiply(launch).setY(launchY));
                executeActions(player, actions);
            }
        }
    }

}
