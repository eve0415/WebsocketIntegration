package net.eve0415.spigot.WebsocketIntegration;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import net.eve0415.spigot.WebsocketIntegration.websocket.EventState;

public class commandHandler {
    private main instance;

    public commandHandler(main instance) {
        this.instance = instance;
    }

    public void processer(JSONObject obj) {
        try {
            Player player = Bukkit.getPlayer(UUID.fromString(obj.getString("UUID")));

            if (player == null)
                return;

            String command = obj.getString("message");

            new BukkitRunnable() {
                @Override
                public void run() {
                    boolean result = player.performCommand(command);

                    instance.websocketManager.send(EventState.COMMAND, player, String.valueOf(result));
                }
            }.runTaskLater(this.instance, 20);
        } catch (JSONException e) {
            e.printStackTrace();
            this.instance.getLogger().warning(e.toString());
        }
    }
}