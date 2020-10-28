package net.eve0415.spigot.WebsocketIntegration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.LiteralText.Builder;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import net.eve0415.spigot.WebsocketIntegration.Util.WSIChatHandler;

public class WSISpongeChatSender implements WSIChatHandler {
    @Override
    public void chatHandler(final String name, final String uuid, final String url, final String message) {
        final String mes = WSIChatHandler.messageFormatter(getName(name, uuid), message);
        final Builder component = Text.builder(mes);

        if (!url.equals("null")) {
            try {
                component
                        .onHover(TextActions.showText(Text.of("Click to open the website!")))
                        .onClick(TextActions.openUrl(new URL(url)));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        Sponge.getServer().getBroadcastChannel().send(component.build());
    }

    @Override
    public String getName(final String name, final String uuid) {
        if (uuid.equals("null")) return name;
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
        return Sponge.getServer().getPlayer(UUID.fromString(uuid)).isPresent()
                ? Sponge.getServer().getPlayer(UUID.fromString(uuid)).get().getName()
                : userStorage.get().get(uuid).get().getName();
    }
}
