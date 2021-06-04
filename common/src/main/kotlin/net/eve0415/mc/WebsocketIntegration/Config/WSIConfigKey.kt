package net.eve0415.spigot.WebsocketIntegration.Config

import kotlin.properties.Delegates

public class WSIConfigKey {
  lateinit var address: String
  var port: Int by Delegates.notNull()
  var id: Int by Delegates.notNull()
  lateinit var name: String
}
