package facade

import org.apache.spark.sql.SparkSession
import statistics.{AllCountryStatistics, SingleCountryStatistics}

class Statistics(final val sparkSession: SparkSession,final val covid19FilePath: String,final val outputData: String = "src/resources/outputDataPaths.json") {

  private val percentagesStats = new AllCountryStatistics(sparkSession, covid19FilePath)
  private val singleCountryStats = new SingleCountryStatistics(sparkSession, covid19FilePath, outputData)

  def singleCountry(): SingleCountryStatistics = {
    singleCountryStats
  }

  def allCountries(): AllCountryStatistics = {
    percentagesStats
  }


}
