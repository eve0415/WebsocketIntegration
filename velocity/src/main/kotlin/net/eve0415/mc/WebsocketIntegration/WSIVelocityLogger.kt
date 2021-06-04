package net.eve0415.mc.WebsocketIntegration

import net.eve0415.mc.WebsocketIntegration.Interface.WSILogger
import org.slf4j.Logger

public class WSIVelocityLogger constructor(val logger: Logger) : WSILogger {
  override fun error(message: String, error: Throwable) {
    logger.error(message, error)
  }

  override fun error(message: String) {
    logger.error(message)
  }

  override fun warning(message: String) {
    logger.warn(message)
  }

  override fun info(message: String) {
    logger.info(message)
  }
}
