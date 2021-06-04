package net.eve0415.mc.WebsocketIntegration.Config

import java.io.File
import java.nio.file.Files
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor

public class WSIConfigFile {
  companion object {
    fun load(file: File): WSIConfigKey {
      if (!file.exists())
          Files.copy(WSIConfigFile::class.java.getResourceAsStream("/config.yml"), file.toPath())

      return Yaml(CustomClassLoaderConstructor(WSIConfigKey::class.java.classLoader))
          .loadAs(file.reader(), WSIConfigKey::class.java)
    }
  }
}
