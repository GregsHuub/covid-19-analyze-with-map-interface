package json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scala.io.Source

class JsonMapperUtils {

  /**
   *
   * @param jsonFilePath () -> path to json file
   * @return Map[String, String/Map]
   */
  def jsonFileToMap(jsonFilePath: String): Map[String, Object] = {
    val json = Source.fromFile(jsonFilePath)
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.readValue[Map[String, Object]](json.reader())
  }
}

