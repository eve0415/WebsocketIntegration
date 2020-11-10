package net.eve0415.spigot.WebsocketIntegration;

import java.util.UUID;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.json.JSONException;

import net.eve0415.spigot.WebsocketIntegration.Util.LogEventType;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;

public class PaperEventListener implements Listener {
    public PaperEventListener(final WSIPaperPlugin instance) {
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    private void sendChatMessage(final String name, final UUID uuid, final String message) {
        try {
            WebsocketManager.getInstance().send(WSIEventState.CHAT,
                    WebsocketManager.builder().message(name, uuid, message).toJSON());
        } catch (final JSONException e) {
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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAdvancementDone(final PlayerAdvancementDoneEvent event) {
        // We don't want to call out receipes unlock as advancement complete.
        if (event.getAdvancement().getKey().getKey().toString().contains("recipes/"))
            return;

        final String adv = event.getAdvancement().getKey().getNamespace().toUpperCase() + "_"
                + event.getAdvancement().getKey().getKey().replace('/', '_').toUpperCase();

        try {
            WebsocketManager.getInstance().send(WSIEventState.ADVANCEMENT,
                    WebsocketManager.builder()
                            .message(event.getPlayer().getName(), event.getPlayer().getUniqueId(), adv).toJSON());
        } catch (final JSONException e) {
            WebsocketManager.getInstance().getWSILogger().error("There was an error trying to send chat message", e);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerPreLogin(final AsyncPlayerPreLoginEvent event) {
        try {
            WebsocketManager.getInstance().send(WSIEventState.LOG,
                    WebsocketManager.builder()
                            .log(LogEventType.AUTH, event.getName(), event.getUniqueId(),
                                    event.getAddress().getHostAddress())
                            .toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLogin(final PlayerLoginEvent event) {
        try {
            if (event.getKickMessage().isEmpty()) {
                WebsocketManager.getInstance().send(WSIEventState.LOG,
                        WebsocketManager.builder()
                                .log(LogEventType.LOGIN, event.getPlayer().getName(), event.getPlayer().getUniqueId(),
                                        event.getAddress().getHostAddress())
                                .setAddress(event.getHostname()).toJSON());
            } else {
                WebsocketManager.getInstance().send(WSIEventState.LOG,
                        WebsocketManager.builder()
                                .log(LogEventType.KICK, event.getPlayer().getName(), event.getPlayer().getUniqueId(),
                                        event.getHostname())
                                .kick(event.getKickMessage()).toJSON());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(final PlayerKickEvent event) {
        try {
            WebsocketManager.getInstance().send(WSIEventState.LOG,
                    WebsocketManager.builder()
                            .log(LogEventType.KICK, event.getPlayer().getName(), event.getPlayer().getUniqueId(),
                                    event.getPlayer().getAddress().getHostName())
                            .kick(event.getReason()).toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerConnectionClose(final PlayerConnectionCloseEvent event) {
        try {
            WebsocketManager.getInstance().send(WSIEventState.LOG,
                    WebsocketManager.builder()
                            .log(LogEventType.DISCONNECT, event.getPlayerName(), event.getPlayerUniqueId(),
                                    event.getIpAddress().getHostName())
                            .toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
