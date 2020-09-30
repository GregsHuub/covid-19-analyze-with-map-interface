import java.lang.System._

import facade.Statistics
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
 * @author Grzegorz Ozimski
 * @version 1.0
 */
object Main {

  def main(args: Array[String]): Unit = {
    setProperty("hadoop.home.dir", "C:\\hadoop");
    val resourcePath = "src/resources/covid-19-csv/"
    val sourceRoot = "src/resources/outputDataPaths.json"
    val conf = "covid-19-confirmed.csv"
    val confWide = "covid19_confirmed-wide.csv"
    val die = "covid-19-death.csv"
    val recovered = "covid-19-recovered.csv"

    Logger.getLogger("org").setLevel(Level.ERROR)
    val ss: SparkSession = SparkSession.builder().appName("covid-19").master("local[*]").getOrCreate()

    val stats = new Statistics(ss, resourcePath + conf)
    val confirm = stats.singleCountry().confirmedStatistics()
    val statsD = new Statistics(ss, resourcePath + die)
    val death = statsD.singleCountry().deathStatistics()
    val all = stats.allCountries().percentageIncreaseInAll()
    val singlePerc = stats.allCountries().percentageIncreaseInCountries()
    println("Poland")
    singlePerc.select("*").where(singlePerc.col("Country/Region") === "Poland").show(10)
    all.show(100)
    println(death)
    println(confirm)


  }
}
