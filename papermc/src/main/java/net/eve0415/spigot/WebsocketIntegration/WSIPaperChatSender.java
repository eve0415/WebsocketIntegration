package net.eve0415.spigot.WebsocketIntegration;

import java.util.UUID;

import net.eve0415.spigot.WebsocketIntegration.Util.WSIChatHandler;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class WSIPaperChatSender implements WSIChatHandler {
    private final WSIPaperPlugin instance;

    public WSIPaperChatSender(final WSIPaperPlugin instance) {
        this.instance = instance;
    }

    @Override
    public void chatHandler(final String name, final String uuid, final String url, final String message) {
        final TextComponent component = new TextComponent(
                WSIChatHandler.messageFormatter(getName(name, uuid), message));

        if (!url.equals("null")) {
            component
                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to open the website!")));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        }

        instance.getServer().broadcast(component);
    }

    @Override
    public String getName(final String name, final String uuid) {
        if (uuid.equals("null")) return name;
        return instance.getServer().getPlayer(UUID.fromString(uuid)) == null
                ? instance.getServer().getOfflinePlayer(UUID.fromString(uuid)).getName()
                : instance.getServer().getPlayer(UUID.fromString(uuid)).getName();
    }
}
