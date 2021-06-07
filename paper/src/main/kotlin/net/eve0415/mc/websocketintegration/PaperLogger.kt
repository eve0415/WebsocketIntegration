package net.eve0415.mc.websocketintegration

import net.eve0415.mc.websocketintegration.constructor.WILogger
import java.util.logging.Level
import java.util.logging.Logger

class PaperLogger constructor(private val logger: Logger) : WILogger {
  override fun error(message: String, error: Throwable) {
    logger.log(Level.WARNING, message, error)
  }

  override fun error(message: String) {
    logger.warning(message)
  }

  override fun warning(message: String) {
    logger.warning(message)
  }

  override fun info(message: String) {
    logger.info(message)
  }
}
