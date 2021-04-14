package net.eve0415.spigot.WebsocketIntegration;

import java.util.UUID;

import net.eve0415.spigot.WebsocketIntegration.Util.WSIChatHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

public class WSIPaperChatSender implements WSIChatHandler {
    private final WSIPaperPlugin instance;

    public WSIPaperChatSender(final WSIPaperPlugin instance) {
        this.instance = instance;
    }

    @Override
    public void chatHandler(final String name, final String uuid, final String url, final String message) {
        final String mes = WSIChatHandler.messageFormatter(getName(name, uuid), message);
        final TextComponent component = Component.text(mes);

        if (!url.equals("null")) {
            component
                    .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                            Component.text("Click to open the website!")))
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, url));
        }

        instance.getServer().sendMessage(component);
        // Using TextComponent, it does not send to console so we do this way
        instance.getServer().getConsoleSender().sendMessage(mes);
    }

    @Override
    public String getName(final String name, final String uuid) {
        if (uuid.equals("null"))
            return name;
        return instance.getServer().getPlayer(UUID.fromString(uuid)) == null
                ? instance.getServer().getOfflinePlayer(UUID.fromString(uuid)).getName()
                : instance.getServer().getPlayer(UUID.fromString(uuid)).getName();
    }
}
