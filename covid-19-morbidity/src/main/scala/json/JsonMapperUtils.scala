package json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import scala.io.Source

/**
 * @author Grzegorz Ozimski
 * @version 1.0
 */
class JsonMapperUtils {

  /**
   * @author Grzegorz Ozimski
   * @version 1.0
   * @param jsonFilePath () -> Json file path
   * @return Map[String, String/Map]
   */
  def jsonFileToMap(jsonFilePath: String): Map[String, Object] = {
    val json = Source.fromFile(jsonFilePath)
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.readValue[Map[String, Object]](json.reader())
  }
}

