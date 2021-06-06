package net.eve0415.mc.WebsocketIntegration.Config

import kotlin.properties.Delegates

class WIConfigKey {
  lateinit var address: String
  var port: Int by Delegates.notNull()
  var id: Int by Delegates.notNull()
  lateinit var name: String
}
