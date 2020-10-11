package net.eve0415.spigot.WebsocketIntegration.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eve0415.spigot.WebsocketIntegration.main;
import net.eve0415.spigot.WebsocketIntegration.websocket.EventState;

public class link implements CommandExecutor {
    private final main instance;

    public link(final main instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはプレイヤーのみ使用することができます");
            return true;
        }

        final Player player = (Player) sender;

        if (args.length == 0) {
            this.instance.websocketManager.send(EventState.LINK, player, null);
            return true;
        } else if (args.length == 1) {
            this.instance.websocketManager.send(EventState.LINK, player, args[0]);
            return true;
        }

        sender.sendMessage("コマンドを見直してから再実行してください。");
        return false;
    }
}
