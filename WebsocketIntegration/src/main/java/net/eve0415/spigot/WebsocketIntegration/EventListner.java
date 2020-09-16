package net.eve0415.spigot.WebsocketIntegration;

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
    private final main instance;

    public EventListner(final main instance) {
        this.instance = instance;
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(final PlayerJoinEvent event) {
        instance.websocketManager.send(EventState.JOIN, event.getPlayer(), event.getJoinMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(final PlayerQuitEvent event) {
        instance.websocketManager.send(EventState.QUIT, event.getPlayer(), event.getQuitMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(final PlayerDeathEvent event) {
        instance.websocketManager.send(EventState.DEATH, event.getEntity(), event.getDeathMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMessage(final AsyncPlayerChatEvent event) {
        instance.websocketManager.send(EventState.MESSAGE, event.getPlayer(), event.getMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAdvancementDone(final PlayerAdvancementDoneEvent event) {
        if (event.getAdvancement().getKey().getKey().toString().contains("recipes/"))
            return;

        final String adv = event.getAdvancement().getKey().getNamespace().toUpperCase() + "_"
                + event.getAdvancement().getKey().getKey().replace('/', '_').toUpperCase();

        instance.websocketManager.send(EventState.MESSAGE, event.getPlayer(),
                event.getPlayer().getName() + " は挑戦 [" + Advancement.valueOf(adv).getValue() + "] を完了した");
    }
}