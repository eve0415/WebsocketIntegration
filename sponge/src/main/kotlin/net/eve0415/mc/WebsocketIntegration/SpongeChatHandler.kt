package net.eve0415.mc.WebsocketIntegration

import java.net.URL
import java.util.UUID
import net.eve0415.mc.WebsocketIntegration.Interface.WIChatHandler
import org.spongepowered.api.Sponge
import org.spongepowered.api.service.user.UserStorageService
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.action.TextActions

class SpongeChatHandler : WIChatHandler {
  override fun chatHandler(name: String, uuid: String, url: String, message: String) {
    val mes = messageFormatter(getName(name, uuid), message)
    val component = Text.builder(mes)

    if (!url.equals("null"))
        component
            .onHover(TextActions.showText(Text.of("Click to open the website!")))
            .onClick(TextActions.openUrl(URL(url)))

    Sponge.getServer().broadcastChannel.send(component.build())
  }

  override fun getName(name: String, uuid: String): String {
    if (uuid.equals("null")) return name
    val userStorage = Sponge.getServiceManager().provide(UserStorageService::class.java)
    return Sponge.getServer().getPlayer(UUID.fromString(uuid)).get().name
        ?: userStorage.get().get(uuid).get().name
  }
}
