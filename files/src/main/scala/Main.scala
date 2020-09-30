import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SparkSession}

object Main {

  def main(args: Array[String]): Unit = {

    val resourcePath = "src/resources/covid-19-csv/"
    val wideDeaths = "time_series_2019-ncov-Deaths-wide.csv"
    val narrowDeaths = "time_series-ncov-Deaths-narrow.csv"
    val narrowRecovered = "time_series-ncov-Recovered-narrow.csv"
    val narrowConfirmed = "time_series-ncov-Confirmed-narrow.csv"
    val sourceRoot = "src/resources/outputDataPaths.json"

//    Logger.getLogger("org").setLevel(Level.ERROR)
    println("tak")

//    val getData = new GetData()
//    getData.getDataFromUrl("downloadAll", sourceRoot)

//    val ss: SparkSession = SparkSession.builder().appName("covid-19").master("local").getOrCreate()


//    actualDeathsStatistics(ss, resourcePath, narrowDeaths)

  }
  //    val schema = new StructType()
  //      .add(StructField("Province/State", StringType, true))
  //      .add(StructField("Country/Region", StringType, false))
  //      .add(StructField("Lat", DoubleType, false))
  //      .add(StructField("Long", DoubleType, false))
  //      .add(StructField("Date", DoubleType, false))
  //      .add(StructField("Value", DoubleType, false))
  def actualDeathsStatistics(sparkSession: SparkSession, resourcePath: String, fileWithDeathsStatistics: String): Unit = {

    val ss: SparkSession = SparkSession.builder().appName("covid-19").master("local").getOrCreate()

    val dayBeforeCurrent = LocalDateTime.now().minusDays(4).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    val file = sparkSession.read.option("header", "true").csv(resourcePath + fileWithDeathsStatistics)
    file.printSchema()
    val df = file.select("Province/State", "Country/Region", "Date", "Value")
    val sortedAndGroupedByCountry = df.select(df.col("*"))
      .where(df.col("Date") === dayBeforeCurrent)
      .where(df.col("Value") > 0)
    sortedAndGroupedByCountry
      .sort("Value")
      .take(Int.MaxValue).foreach(println)

  }
}
