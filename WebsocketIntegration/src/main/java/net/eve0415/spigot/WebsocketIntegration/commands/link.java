package net.eve0415.spigot.WebsocketIntegration.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eve0415.spigot.WebsocketIntegration.main;
import net.eve0415.spigot.WebsocketIntegration.websocket.EventState;

public class link implements CommandExecutor {
    private main instance;

    public link(main instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはプレイヤーのみ使用することができます");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            this.instance.webhookManager.send(EventState.LINK, player, null);
            return true;
        } else if (args.length == 1) {
            this.instance.webhookManager.send(EventState.LINK, player, args[0]);
            return true;
        }

        sender.sendMessage("コマンドを見直してから再実行してください。");
        return false;
    }
}