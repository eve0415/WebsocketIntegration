package net.eve0415.spigot.WebsocketIntegration;

import java.util.Optional;
import java.util.UUID;

import org.json.JSONException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;

public class SpongeEventListener {
    public SpongeEventListener(final WSISpongePlugin instance) {
        Sponge.getEventManager().registerListeners(instance, this);
    }

    private void sendChatMessage(final String name, final UUID uuid, final String message) {
        try {
            WebsocketManager.getInstance().send(WSIEventState.CHAT,
                    WebsocketManager.builder().message(name, uuid, message).toJSON());
        } catch (final JSONException e) {
            WebsocketManager.getInstance().getWSILogger().error("There was an error trying to send chat message", e);
        }
    }

    @Listener
    public void onJoin(final ClientConnectionEvent.Join event) {
        sendChatMessage(event.getTargetEntity().getName(), event.getTargetEntity().getUniqueId(),
                event.getMessage().toPlain());
    }

    @Listener
    public void onQuit(final ClientConnectionEvent.Disconnect event) {
        sendChatMessage(event.getTargetEntity().getName(), event.getTargetEntity().getUniqueId(),
                event.getMessage().toPlain());
    }

    @Listener
    public void onDeath(final DestructEntityEvent.Death event) {
        if (!(event.getTargetEntity() instanceof Player)) return;
        final Player player = (Player) event.getTargetEntity();
        sendChatMessage(player.getName(), player.getUniqueId(),
                event.getMessage().toPlain());
    }

    @Listener
    public void onMessage(final MessageChannelEvent.Chat event) {
        final Optional<Player> optPlayer = event.getCause().first(Player.class);
        final String pl = "<" + optPlayer.get().getName() + "> ";
        sendChatMessage(optPlayer.get().getName(), optPlayer.get().getUniqueId(), event.getMessage().toPlain()
                .substring(pl.length()));
    }
}
