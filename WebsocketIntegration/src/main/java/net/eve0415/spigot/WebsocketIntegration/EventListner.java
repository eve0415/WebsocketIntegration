package net.eve0415.spigot.WebsocketIntegration;

import java.util.regex.Pattern;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.eve0415.spigot.WebsocketIntegration.websocket.EventState;

public class EventListner implements Listener {
    private main instance;

    public EventListner(main instance) {
        this.instance = instance;
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    private static String normalize(String string) {
        StringBuilder sb = new StringBuilder();
        for (String s : string.split(Pattern.quote("_"))) {
            sb.append(s.charAt(0)).append(s.substring(1).toLowerCase()).append(" ");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        instance.webhookManager.send(EventState.JOIN, event.getPlayer(), event.getJoinMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        instance.webhookManager.send(EventState.QUIT, event.getPlayer(), event.getQuitMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        instance.webhookManager.send(EventState.DEATH, event.getEntity(), event.getDeathMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMessage(AsyncPlayerChatEvent event) {
        instance.webhookManager.send(EventState.MESSAGE, event.getPlayer(), event.getMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {
        if (event.getAdvancement().getKey().getKey().contains("recipe/"))
            return;

        instance.webhookManager.send(EventState.MESSAGE, event.getPlayer(),
                normalize(event.getAdvancement().toString()));
    }
}