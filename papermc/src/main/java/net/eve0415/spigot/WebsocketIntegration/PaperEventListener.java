package net.eve0415.spigot.WebsocketIntegration;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.json.JSONException;

import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;

public class PaperEventListener implements Listener {
    public PaperEventListener(final WSIPaperPlugin instance) {
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    private void sendChatMessage(String name, UUID uuid, String message) {
        try {
            WebsocketManager.getInstance().send(WSIEventState.CHAT,
                    WebsocketManager.builder().name(name).UUID(uuid).message(message));
        } catch (JSONException e) {
            WebsocketManager.getInstance().getWSILogger().error("There was an error trying to send chat message", e);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(final PlayerJoinEvent event) {
        sendChatMessage(event.getPlayer().getName(), event.getPlayer().getUniqueId(), event.getJoinMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(final PlayerQuitEvent event) {
        sendChatMessage(event.getPlayer().getName(), event.getPlayer().getUniqueId(), event.getQuitMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(final PlayerDeathEvent event) {
        sendChatMessage(event.getEntity().getName(), event.getEntity().getUniqueId(), event.getDeathMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMessage(final AsyncPlayerChatEvent event) {
        sendChatMessage(event.getPlayer().getName(), event.getPlayer().getUniqueId(), event.getMessage());
    }
}
