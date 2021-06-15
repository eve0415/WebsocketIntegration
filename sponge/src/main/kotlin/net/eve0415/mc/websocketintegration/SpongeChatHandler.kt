package net.eve0415.mc.websocketintegration

import net.eve0415.mc.websocketintegration.constructor.WIChatHandler
import org.spongepowered.api.Sponge
import org.spongepowered.api.service.user.UserStorageService
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.action.TextActions
import java.net.URL
import java.util.*

class SpongeChatHandler : WIChatHandler {
  override fun send(name: String, uuid: String, url: String, message: String) {
    val mes = messageFormatter(getName(name, uuid), message)
    val component = Text.builder(mes)

    if (url != "null")
      component
        .onHover(TextActions.showText(Text.of("Click to open the website!")))
        .onClick(TextActions.openUrl(URL(url)))

    Sponge.getServer().broadcastChannel.send(component.build())
  }

  override fun getName(name: String, uuid: String): String {
    if (uuid == "null") return name
    val userStorage = Sponge.getServiceManager().provide(UserStorageService::class.java)
    return Sponge.getServer().getPlayer(UUID.fromString(uuid)).get().name
      ?: userStorage.get().get(uuid).get().name
  }
}
