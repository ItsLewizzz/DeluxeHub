package fun.lewisdev.deluxehub.cooldown;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.UUID;

public class CooldownManager {

    private Table<String, CooldownType, Long> cooldowns = HashBasedTable.create();

    /**
     * Retrieve the number of milliseconds left until a given cooldown expires.
     * <p>
     * Check for a negative value to determine if a given cooldown has expired. <br>
     * Cooldowns that have never been defined will return {@link Long#MIN_VALUE}.
     *
     * @param uuid - the uuid of the player.
     * @param key  - cooldown to locate.
     * @return Number of milliseconds until the cooldown expires.
     */
    public long getCooldown(UUID uuid, CooldownType key) {
        return calculateRemainder(cooldowns.get(uuid.toString(), key));
    }

    /**
     * Update a cooldown for the specified player.
     *
     * @param uuid  - uuid of the player.
     * @param key   - cooldown to update.
     * @param delay - number of milliseconds until the cooldown will expire again.
     * @return The previous number of milliseconds until expiration.
     */
    public long setCooldown(UUID uuid, CooldownType key, long delay) {
        return calculateRemainder(cooldowns.put(uuid.toString(), key, System.currentTimeMillis() + (delay * 1000)));
    }

    /**
     * Determine if a given cooldown has expired. If it has, refresh the cooldown. If not, do nothing.
     *
     * @param uuid  - uuid of the player.
     * @param key   - cooldown to update.
     * @param delay - number of milliseconds until the cooldown will expire again.
     * @return TRUE if the cooldown was expired/unset and has now been reset, FALSE otherwise.
     */
    public boolean tryCooldown(UUID uuid, CooldownType key, long delay) {
        if (getCooldown(uuid, key) / 1000 > 0) return false;
        setCooldown(uuid, key, delay + 1);
        return true;
    }

    /**
     * Remove any cooldowns associated with the given player.
     *
     * @param uuid - the uuid of the player we will reset.
     */
    public void removeCooldowns(UUID uuid) {
        cooldowns.row(uuid.toString()).clear();
    }

    private long calculateRemainder(Long expireTime) {
        return expireTime != null ? expireTime - System.currentTimeMillis() : Long.MIN_VALUE;
    }
}