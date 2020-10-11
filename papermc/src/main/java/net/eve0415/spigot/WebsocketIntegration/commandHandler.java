package net.eve0415.spigot.WebsocketIntegration;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import net.eve0415.spigot.WebsocketIntegration.websocket.EventState;

public class commandHandler {
    private final main instance;

    public commandHandler(final main instance) {
        this.instance = instance;
    }

    public void processer(final JSONObject obj) {
        try {
            final Player player = Bukkit.getPlayer(UUID.fromString(obj.getString("UUID")));

            if (player == null)
                return;

            final String command = obj.getString("message");

            new BukkitRunnable() {
                @Override
                public void run() {
                    final boolean result = player.performCommand(command);

                    instance.websocketManager.send(EventState.COMMAND, player, String.valueOf(result));
                }
            }.runTaskLater(this.instance, 20);
        } catch (final JSONException e) {
            e.printStackTrace();
            this.instance.getLogger().warning(e.toString());
        }
    }
}
