package net.eve0415.spigot.WebsocketIntegration;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONException;
import org.json.JSONObject;

import net.md_5.bungee.api.ChatColor;

public class linkManager {
    private final main instance;

    public linkManager(final main instance) {
        this.instance = instance;
    }

    public void processer(final JSONObject obj) {
        try {
            final String state = obj.getString("state");
            final String result = obj.getString("result");
            final Player player = Bukkit.getPlayer(UUID.fromString(obj.getString("UUID")));

            String message;

            if (state.equals("linking")) {
                message = "今から1分以内に、 Discordに " + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.YELLOW + "!link "
                        + result + ChatColor.RESET + " を送信してください";
            } else {
                message = result;
            }
            this.instance.sender.privateMessage(player, message);
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

}