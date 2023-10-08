/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Crypto Morin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fun.lewisdev.deluxehub.utility.reflection;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Objects;
import java.util.concurrent.Callable;

/*
 * References
 *
 * * * GitHub: https://github.com/CryptoMorin/XSeries/blob/master/ActionBar.java
 * * XSeries: https://www.spigotmc.org/threads/378136/
 * PacketPlayOutTitle: https://wiki.vg/Protocol#Title
 *
 */

/**
 * A reflection API for action bars in Minecraft.
 * Fully optimized - Supports 1.8.8+ and above.
 * Requires ReflectionUtils.
 * Messages are not colorized by default.
 * <p>
 * Action bars are text messages that appear above
 * the player's <a href="https://minecraft.wiki/w/Heads-up_display">hotbar</a>
 * Note that this is different than the text appeared when switching between items.
 * Those messages show the item's name and are different from action bars.
 * The only natural way of displaying action bars is when mounting.
 *
 * @author Crypto Morin
 * @version 1.0.0
 * @see ReflectionUtils
 */
public class ActionBar {

    private static final JavaPlugin PLUGIN = JavaPlugin.getProvidingPlugin(DeluxeHubPlugin.class);
    private static final MethodHandle CHAT_COMPONENT_TEXT;
    private static final MethodHandle PACKET;
    private static final Object CHAT_MESSAGE_TYPE;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Class<?> packetPlayOutChatClass = ReflectionUtils.getNMSClass("PacketPlayOutChat");
        Class<?> iChatBaseComponentClass = ReflectionUtils.getNMSClass("IChatBaseComponent");

        MethodHandle packet = null;
        MethodHandle chatComp = null;
        Object chatMsgType = null;

        try {
            // Game Info Message Type
            Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + ReflectionUtils.VERSION + ".ChatMessageType");
            for (Object obj : chatMessageTypeClass.getEnumConstants()) {
                if (obj.toString().equals("GAME_INFO")) {
                    chatMsgType = obj;
                    break;
                }
            }

            // JSON Message Builder
            Class<?> chatComponentTextClass = ReflectionUtils.getNMSClass("ChatComponentText");
            chatComp = lookup.findConstructor(chatComponentTextClass, MethodType.methodType(void.class, String.class));

            // Packet Constructor
            packet = lookup.findConstructor(packetPlayOutChatClass, MethodType.methodType(void.class, iChatBaseComponentClass, chatMessageTypeClass));
        } catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException ignored) {
            try {
                // Game Info Message Type
                chatMsgType = (byte) 2;

                // JSON Message Builder
                Class<?> chatComponentTextClass = ReflectionUtils.getNMSClass("ChatComponentText");
                chatComp = lookup.findConstructor(chatComponentTextClass, MethodType.methodType(void.class, String.class));

                // Packet Constructor
                packet = lookup.findConstructor(packetPlayOutChatClass, MethodType.methodType(void.class, iChatBaseComponentClass, byte.class));
            } catch (NoSuchMethodException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        CHAT_MESSAGE_TYPE = chatMsgType;
        CHAT_COMPONENT_TEXT = chatComp;
        PACKET = packet;
    }

    /**
     * Sends an action bar to a player.
     *
     * @param player  the player to send the action bar to.
     * @param message the message to send.
     * @see #sendActionBar(Player, String, long)
     * @since 1.0.0
     */
    public static void sendActionBar(Player player, String message) {
        Objects.requireNonNull(player, "Cannot send action bar to null player");
        Object packet = null;

        try {
            Object component = CHAT_COMPONENT_TEXT.invoke(message);
            packet = PACKET.invoke(component, CHAT_MESSAGE_TYPE);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        ReflectionUtils.sendPacket(player, packet);
    }

    /**
     * Sends an action bar all the online players.
     *
     * @param message the message to send.
     * @see #sendActionBar(Player, String)
     * @since 1.0.0
     */
    public static void sendAllActionBar(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) sendActionBar(player, message);
    }

    /**
     * Sends an action bar to a player for a specific amount of ticks.
     * Plugin instance should be changed in this method for the schedulers.
     * <p>
     * If the caller returns true, the action bar will continue.
     * If the caller returns false, action bar will not be sent anymore.
     *
     * @param player   the player to send the action bar to.
     * @param message  the message to send. The message will not be updated.
     * @param callable the condition for the action bar to continue.
     * @see #sendActionBar(Player, String, long)
     * @since 1.0.0
     */
    public static void sendActionBarWhile(Player player, String message, Callable<Boolean> callable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (!callable.call()) {
                        cancel();
                        return;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                sendActionBar(player, message);
            }
            // Re-sends the messages every 2 seconds so it doesn't go away from the player's screen.
        }.runTaskTimerAsynchronously(PLUGIN, 0L, 40L);
    }

    /**
     * Sends an action bar to a player for a specific amount of ticks.
     * <p>
     * If the caller returns true, the action bar will continue.
     * If the caller returns false, action bar will not be sent anymore.
     *
     * @param player   the player to send the action bar to.
     * @param message  the message to send. The message will be updated.
     * @param callable the condition for the action bar to continue.
     * @see #sendActionBarWhile(Player, String, Callable)
     * @since 1.0.0
     */
    public static void sendActionBarWhile(Player player, Callable<String> message, Callable<Boolean> callable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (!callable.call()) {
                        cancel();
                        return;
                    }
                    sendActionBar(player, message.call());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            // Re-sends the messages every 2 seconds so it doesn't go away from the player's screen.
        }.runTaskTimerAsynchronously(PLUGIN, 0L, 40L);
    }

    /**
     * Sends an action bar to a player for a specific amount of ticks.
     *
     * @param player   the player to send the action bar to.
     * @param message  the message to send.
     * @param duration the duration to keep the action bar in ticks.
     * @see #sendActionBarWhile(Player, String, Callable)
     * @since 1.0.0
     */
    public static void sendActionBar(Player player, String message, long duration) {
        if (duration < 1) return;

        new BukkitRunnable() {
            long repeater = duration;

            @Override
            public void run() {
                sendActionBar(player, message);
                repeater -= 40L;
                if (repeater - 40L < -20L) cancel();
            }
            // Re-sends the messages every 2 seconds so it doesn't go away from the player's screen.
        }.runTaskTimerAsynchronously(PLUGIN, 0L, 40L);
    }
}