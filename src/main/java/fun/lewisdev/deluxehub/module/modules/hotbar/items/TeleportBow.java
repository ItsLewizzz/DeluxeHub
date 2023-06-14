package fun.lewisdev.deluxehub.module.modules.hotbar.items;

import de.tr7zw.changeme.nbtapi.NBTItem;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.module.modules.hotbar.HotbarItem;
import fun.lewisdev.deluxehub.module.modules.hotbar.HotbarManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;

public class TeleportBow extends HotbarItem {

    private final int cooldown;
    private final int arrowSlot;

    public TeleportBow(HotbarManager hotbarManager, ItemStack item, int slot, String key) {
        super(hotbarManager, item, slot, key);

        FileConfiguration config = getHotbarManager().getConfig(ConfigType.SETTINGS);

        this.cooldown = config.getInt("teleport_bow.cooldown", 3);
        this.arrowSlot = config.getInt("teleport_bow.arrow_slot", 9);
    }

    @Override
    protected void onInteract(Player player) {
        // DO NOTHING
    }

    @Override
    public void giveItem(Player player) {
        super.giveItem(player);

        // Ensure the player has permission to use this item
        if (getPermission() != null && !player.hasPermission(getPermission())) {
            return;
        }

        ItemStack arrow = new ItemStack(org.bukkit.Material.ARROW);

        // Give the player arrows
        player.getInventory().setItem(arrowSlot, arrow);
    }

    @EventHandler
    public void onEntityLaunch(ProjectileLaunchEvent event) {
        Projectile entity = event.getEntity();
        if (getHotbarManager().inDisabledWorld(entity.getWorld())) {
            return;
        }

        if (!(entity.getShooter() instanceof Player) || !(entity instanceof Arrow)) {
            return;
        }

        Player player = (Player) entity.getShooter();
        PlayerInventory inventory = player.getInventory();
        ItemStack itemInMainHand = inventory.getItemInMainHand();
        String hotbarItem = new NBTItem(itemInMainHand).getString("hotbarItem");
        if (hotbarItem == null || !hotbarItem.equals(getKey())) {
            return;
        }

        // Apply cooldown
        player.setCooldown(itemInMainHand.getType(), cooldown * 20);
        // Add entity metadata to be used in ProjectileHitEvent
        entity.setMetadata("teleportEntity", new FixedMetadataValue(getPlugin(), player.getUniqueId()));
    }

    @EventHandler
    public void onPlayerDamageItem(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        // Ensure the player has permission to use this item
        if (getPermission() != null && !player.hasPermission(getPermission())) {
            return;
        }

        ItemStack item = event.getItem();
        String hotbarItem = new NBTItem(item).getString("hotbarItem");
        if (hotbarItem == null || !hotbarItem.equals(getKey())) {
            return;
        }

        // Cancel the event, since the player is not supposed to take damage
        event.setCancelled(true);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        if (getHotbarManager().inDisabledWorld(entity.getWorld())) {
            return;
        }

        if (!(entity instanceof Arrow)) {
            return;
        }

        System.out.println("Projectile hit");

        if (!entity.hasMetadata("teleportEntity")) {
            return;
        }

        ProjectileSource shooter = entity.getShooter();
        if (!(shooter instanceof Player)) {
            return;
        }

        BlockFace hitBlockFace = event.getHitBlockFace();
        Block block = event.getHitBlock();
        // Ensure the arrow hit a block
        if (hitBlockFace == null || block == null) {
            return;
        }

        // Get the Location of the block using hitBlockFace & block
        Location directionLocation = block.getLocation().add(hitBlockFace.getDirection());
        Location location = directionLocation.add(0.5, hitBlockFace.getModY() * 1.5, 0.5);

        Player player = (Player) shooter;

        // Teleport the player
        player.teleport(location);

        // Spawn particles
        player.getWorld().spawnParticle(org.bukkit.Particle.PORTAL, directionLocation, 100);

        // Remove the arrow
        entity.remove();
    }

}
