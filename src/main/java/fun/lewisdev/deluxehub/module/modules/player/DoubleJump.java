package fun.lewisdev.deluxehub.module.modules.player;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.config.Messages;
import fun.lewisdev.deluxehub.cooldown.CooldownType;
import fun.lewisdev.deluxehub.module.Module;
import fun.lewisdev.deluxehub.module.ModuleType;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DoubleJump extends Module {

    private long cooldownDelay;
    private double launch;
    private double launchY;
    private List<String> actions;

    private final Map<UUID, Boolean> canJump = new HashMap<>();

    public DoubleJump(DeluxeHubPlugin plugin) {
        super(plugin, ModuleType.DOUBLE_JUMP);
    }

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig(ConfigType.SETTINGS);
        cooldownDelay = config.getLong("double_jump.cooldown", 0);
        launch = config.getDouble("double_jump.launch_power", 1.3);
        launchY = config.getDouble("double_jump.launch_power_y", 1.2);
        actions = config.getStringList("double_jump.actions");

        if (launch > 4.0) launch = 4.0;
        if (launchY > 4.0) launchY = 4.0;
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        // Perform checks
        if (player.hasPermission(new Permission(Permissions.DOUBLE_JUMP_BYPASS.getPermission(), PermissionDefault.FALSE))) return;
        else if (inDisabledWorld(player.getLocation())) return;
        else if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        else if (!event.isFlying()) return;

        UUID uuid = player.getUniqueId();

        // Check if the player has a "jump charge"
        if (canJump.containsKey(uuid) && canJump.get(uuid)) {
            // Check for cooldown
            if (!tryCooldown(uuid, CooldownType.DOUBLE_JUMP, cooldownDelay)) {
                Messages.DOUBLE_JUMP_COOLDOWN.send(player, "%time%", getCooldown(uuid, CooldownType.DOUBLE_JUMP));
                event.setCancelled(true);
                return;
            }

            // Execute double jump
            player.setVelocity(player.getLocation().getDirection().multiply(launch).setY(launchY));
            executeActions(player, actions);

            //Set the players "jump charge" to false
            canJump.remove(uuid);
            canJump.put(uuid, false);
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Set the players "jump charge" to true if they are on solid ground.
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (canJump.containsKey(uuid) && !canJump.get(uuid)) {
            if (((Entity) player).isOnGround()) {
                //Redundant check to prevent hacked clients from spoofing the isOnGround packet. Hacked clients will be able to get a second double jump off a slab regardless of this check. (At that point they might as well fly hack)
                if (player.getWorld().getBlockAt(player.getLocation().subtract(0, 1, 0)).getBlockData().getMaterial().isSolid()) {
                    canJump.remove(uuid);
                    canJump.put(uuid, true);
                }
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR && !inDisabledWorld(player.getLocation())) {
            player.getPlayer().setAllowFlight(true);
            canJump.remove(player.getUniqueId());
            canJump.put(player.getUniqueId(), true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            player.getPlayer().setAllowFlight(true);
            canJump.remove(player.getUniqueId());
            canJump.put(player.getUniqueId(), true);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        canJump.remove(player.getUniqueId());
    }
}
