package fun.lewisdev.deluxehub.hook.hooks.head;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.hook.PluginHook;
import fun.lewisdev.deluxehub.utility.universal.XMaterial;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BaseHead implements PluginHook, HeadHook {

    private Map<String, ItemStack> cache;

    @Override
    public void onEnable(DeluxeHubPlugin plugin) {
        cache = new HashMap<>();
    }

    @Override
    public ItemStack getHead(String data) {
        if (cache.containsKey(data)) return cache.get(data);

        ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", data));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        cache.put(data, head);
        return head;
    }
}
